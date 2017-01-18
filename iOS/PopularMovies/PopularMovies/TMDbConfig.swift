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

// MARK: File Storage Support

private let documentsDirectoryURL: URL = FileManager.default.urls(for: .documentDirectory,
                                                                  in: .userDomainMask).first! as URL
private let fileURL = documentsDirectoryURL.appendingPathComponent("TheMovieDB-Context")

// MARK: - TMDbConfig

final class TMDbConfig: NSObject, NSCoding {
  
  // MARK: Properties
  
  var baseImageURLString = "http://image.tmdb.org/t/p/"
  var secureBaseImageURLString =  "https://image.tmdb.org/t/p/"
  var posterSizes = ["w92", "w154", "w185", "w342", "w500", "w780", "original"]
  var profileSizes = ["w45", "w185", "h632", "original"]
  var dateUpdated: Date? = nil
  
  fileprivate static let webservice = Webservice()
  
  fileprivate static var resource: Resource<TMDbConfig> = {
    return Resource<TMDbConfig>(
      url: TMDbWebservice.urlFrom(parameters: [:], withPathExtension: "/configuration"),
      parseJSON: { result in
        guard let json = result as? JSONDictionary else { return nil }
        return TMDbConfig(json)
    })
  }()
  
  // Returns the number days since the config was last updated.
  var daysSinceLastUpdate: Int? {
    if let lastUpdate = dateUpdated {
      return Int(Date().timeIntervalSince(lastUpdate)) / 60*60*24
    } else {
      return nil
    }
  }
  
  // MARK: Initialization
  
  override init() {}
  
  convenience init?(_ dictionary: JSONDictionary) {
    self.init()
    
    guard let imageDictionary = dictionary["images"] as? JSONDictionary,
      let urlString = imageDictionary["base_url"] as? String,
      let secureURLString = imageDictionary["secure_base_url"] as? String,
      let posterSizesArray = imageDictionary["poster_sizes"] as? [String],
      let profileSizesArray = imageDictionary["profile_sizes"] as? [String] else {
        return nil
    }
    
    baseImageURLString = urlString
    secureBaseImageURLString = secureURLString
    posterSizes = posterSizesArray
    profileSizes = profileSizesArray
    dateUpdated = Date()
  }
  
  // MARK: Update
  
  func fetchNewIfDaysSinceUpdateExceeds(_ days: Int, completion: @escaping (TMDbConfig?) -> ()) {
    if let daysSinceLastUpdate = daysSinceLastUpdate, daysSinceLastUpdate <= days {
      completion(nil)
    } else {
      fetchConfiguration { config in
        config?.save()
        completion(config)
      }
    }
  }
  
  private func fetchConfiguration(_ completion: @escaping (TMDbConfig?) -> ()) {
    TMDbConfig.webservice.load(TMDbConfig.resource) { result in
      switch result {
      case .success(let config):
        completion(config)
      case .error(let error):
        print("Failed to fetch configuration: \(error)")
        completion(nil)
      }
    }
  }
  
  // MARK: - NSCoding -
  
  private let baseImageURLStringKey = "config.base_image_url_string_key"
  private let secureBaseImageURLStringKey =  "config.secure_base_image_url_key"
  private let posterSizesKey = "config.poster_size_key"
  private let profileSizesKey = "config.profile_size_key"
  private let dateUpdatedKey = "config.date_update_key"
  
  required init(coder aDecoder: NSCoder) {
    baseImageURLString = aDecoder.decodeObject(forKey: baseImageURLStringKey) as! String
    secureBaseImageURLString = aDecoder.decodeObject(forKey: secureBaseImageURLStringKey) as! String
    posterSizes = aDecoder.decodeObject(forKey: posterSizesKey) as! [String]
    profileSizes = aDecoder.decodeObject(forKey: profileSizesKey) as! [String]
    dateUpdated = aDecoder.decodeObject(forKey: dateUpdatedKey) as? Date
  }
  
  public func encode(with aCoder: NSCoder) {
    aCoder.encode(baseImageURLString, forKey: baseImageURLStringKey)
    aCoder.encode(secureBaseImageURLString, forKey: secureBaseImageURLStringKey)
    aCoder.encode(posterSizes, forKey: posterSizesKey)
    aCoder.encode(profileSizes, forKey: profileSizesKey)
    aCoder.encode(dateUpdated, forKey: dateUpdatedKey)
  }
  
  private func save() {
    print("New TMDb configuration saved.")
    NSKeyedArchiver.archiveRootObject(self, toFile: fileURL.path)
  }
  
  class func unarchivedInstance() -> TMDbConfig? {
    if FileManager.default.fileExists(atPath: fileURL.path) {
      return NSKeyedUnarchiver.unarchiveObject(withFile: fileURL.path) as? TMDbConfig
    } else {
      return nil
    }
  }
  
}
