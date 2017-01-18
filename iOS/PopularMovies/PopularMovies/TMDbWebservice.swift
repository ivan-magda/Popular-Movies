/**
 * Copyright (c) 2016 Ivan Magda
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import Foundation
import Network

final class TMDbWebservice {
  
  // MARK: Properties
  
  fileprivate let webservice: Webservice
  var config: TMDbConfig
  
  // MARK: Init
  
  init(webservice: Webservice, config: TMDbConfig) {
    self.webservice = webservice
    self.config = config
  }
}

// MARK: - Load Movies -

extension TMDbWebservice {
  
  typealias MoviesSuccessCompletionBlock = ([Movie]) -> ()
  typealias MoviesFailureCompletionBlock = (Error) -> ()
  
  private enum MovieSort {
    case popular, topRated
  }
  
  func popularMovies(success: @escaping MoviesSuccessCompletionBlock,
                     failure: @escaping MoviesFailureCompletionBlock) {
    loadMoviesSorted(by: .popular, success: success, failure: failure)
  }
  
  func topRatedMovies(success: @escaping MoviesSuccessCompletionBlock,
                      failure: @escaping MoviesFailureCompletionBlock) {
    loadMoviesSorted(by: .topRated, success: success, failure: failure)
  }
  
  private func loadMoviesSorted(by sort: MovieSort,
                                success: @escaping MoviesSuccessCompletionBlock,
                                failure: @escaping MoviesFailureCompletionBlock) {
    let parameters = [
      TMDbConstants.TMDBParameterKeys.page: "1",
      TMDbConstants.TMDBParameterKeys.language: TMDbConstants.TMDBParameterValues.languageUS
    ]
    
    var url: URL!
    switch sort {
    case .popular:
      url = TMDbWebservice.urlFrom(parameters: parameters, withPathExtension: "/movie/popular")
    case .topRated:
      url = TMDbWebservice.urlFrom(parameters: parameters, withPathExtension: "/movie/top_rated")
    }
    
    let resource = Resource<[Movie]>(url: url, parseJSON: { result in
      guard let json = result as? JSONDictionary,
        let results = json["results"] as? [JSONDictionary] else { return nil }
      return results.flatMap(Movie.init)
    })
    
    webservice.load(resource, completion: { result in
      switch result {
      case .success(let movies):
        success(movies)
      case .error(let error):
        print("Failed to load movies: \(error)")
        failure(error)
      }
    })
  }
  
}

// MARK: - Media Downloading -

extension TMDbWebservice {
  func imageFor(movie: Movie, completion: @escaping (UIImage?) -> ()) {
    let url = buildImageUrlFor(movie)
    let resource = Resource(url: url, parse: UIImage.init)
    webservice.load(resource) { completion($0.value) }
  }
  
  private static let imageWebservice = CachedWebservice(Webservice())
  
  static func imageFor(movie: Movie, completion: @escaping (UIImage?) -> ()) {
    let url = buildImageUrlFor(movie)
    let resource = Resource(url: url, parse: UIImage.init)
    imageWebservice.load(resource) { completion($0.value) }
  }
}

// MARK: URL Support

extension TMDbWebservice {
  static func urlFrom(parameters: [String: Any], withPathExtension: String? = nil) -> URL {
    var components = URLComponents()
    components.scheme = TMDbConstants.TMDB.apiScheme
    components.host = TMDbConstants.TMDB.apiHost
    components.path = TMDbConstants.TMDB.apiPath + (withPathExtension ?? "")
    components.queryItems = [URLQueryItem]()
    
    components.queryItems!.append(
      URLQueryItem(name: TMDbConstants.TMDBParameterKeys.apiKey,
                   value: TMDbConstants.TMDBParameterValues.apiKey)
    )
    
    for (key, value) in parameters {
      let queryItem = URLQueryItem(name: key, value: "\(value)")
      components.queryItems!.append(queryItem)
    }
    
    return components.url!
  }
  
  func buildImageUrlFor(_ movie: Movie) -> URL {
    return TMDbWebservice.imageUrlForMovie(movie, config: config)
  }
  
  static func buildImageUrlFor(_ movie: Movie) -> URL {
    let config = TMDbConfig.unarchivedInstance() ?? TMDbConfig()
    return imageUrlForMovie(movie, config: config)
  }
  
  private static func imageUrlForMovie(_ movie: Movie, config: TMDbConfig) -> URL {
    let baseUrl = URL(string: config.baseImageURLString)!
    
    var idx = config.profileSizes.count / 2
    if idx <= (config.profileSizes.count - 1) { idx -= 1 }
    
    let sizeUrl = baseUrl.appendingPathComponent(config.profileSizes[idx])
    
    return sizeUrl.appendingPathComponent(movie.posterPath)
  }
}
