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

struct Movie {
    let id: Int
    let posterPath: String
    let overview: String
    let releaseDateString: String
    let genreIds: [Int]
    let title: String
    let isHasVideo: Bool
    let rating: Double
}

extension Movie {
    init?(_ json: [String: Any]) {
        guard let id = json["id"] as? Int,
            let posterPath = json["poster_path"] as? String,
            let overview = json["overview"] as? String,
            let releaseDateString = json["release_date"] as? String,
            let genreIds = json["genre_ids"] as? [Int],
            let title = json["original_title"] as? String,
            let hasVideo = json["video"] as? Bool,
            let rating = json["vote_average"] as? Double else { return nil }
        self.id = id
        self.posterPath = posterPath
        self.overview = overview
        self.releaseDateString = releaseDateString
        self.genreIds = genreIds
        self.title = title
        self.isHasVideo = hasVideo
        self.rating = rating
    }
}
