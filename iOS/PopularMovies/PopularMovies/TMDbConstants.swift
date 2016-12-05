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

// MARK: TMDbConstants

struct TMDbConstants {
    
    // MARK: - TMDB -
    
    struct TMDB {
        static let apiScheme = "https"
        static let apiHost = "api.themoviedb.org"
        static let apiPath = "/3"
    }
    
    // MARK: - TMDB Parameter Keys -
    
    struct TMDBParameterKeys {
        static let apiKey = "api_key"
        static let requestToken = "request_token"
        static let sessionId = "session_id"
        static let username = "username"
        static let password = "password"
        static let page = "page"
        static let language = "language"
    }
    
    // MARK: - TMDB Parameter Values -
    
    struct TMDBParameterValues {
        static let apiKey = "REPLACE_WITH_YOUR_OWN_API_KEY"
        static let languageUS = "en-US"
    }
    
    // MARK: - TMDB Response Keys -
    
    struct TMDBResponseKeys {
        static let title = "title"
        static let id = "id"
        static let posterPath = "poster_path"
        static let statusCode = "status_code"
        static let statusMessage = "status_message"
        static let sessionId = "session_id"
        static let userId = "id"
        static let requestToken = "request_token"
        static let success = "success"
        static let results = "results"
    }
    
}
