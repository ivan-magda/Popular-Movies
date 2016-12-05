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
    
    private let webservice: CachedWebservice
    var config: TMDbConfig
    
    // MARK: Init
    
    init(webservice: CachedWebservice, config: TMDbConfig) {
        self.webservice = webservice
        self.config = config
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
}
