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

import UIKit

// MARK: MovieDetailViewModel

struct MovieDetailViewModel {
    fileprivate let movie: Movie
    
    init(_ movie: Movie) {
        self.movie = movie
    }
}

extension MovieDetailViewModel {
    
    private static var ratingFormatter: NumberFormatter {
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        return formatter
    }
    
    var titleText: String {
        return movie.title.capitalized
    }
    
    var ratingText: String {
        let number = NSDecimalNumber(value: movie.rating)
        guard let ratingString = MovieDetailViewModel.ratingFormatter.string(from: number) else {
            return "Rating: (Undefined)"
        }
        return "Rating: " + ratingString
    }
    
    var overviewText: String {
        return "Overview:\n\(movie.overview)"
    }
    
    var overviewAttributedText: NSAttributedString {
        let overview = NSMutableAttributedString()
        
        let title = NSMutableAttributedString(string: "Overview\n",
                                              attributes: [NSForegroundColorAttributeName: primaryTextColor])
        let paragraphStyle = NSMutableParagraphStyle()
        paragraphStyle.lineSpacing = 6
        title.addAttribute(NSParagraphStyleAttributeName,
                           value: paragraphStyle,
                           range: NSMakeRange(0, title.length))
        
        let text = NSAttributedString(string: movie.overview,
                                      attributes: [NSForegroundColorAttributeName: primaryDarkTextColor])
        overview.append(title)
        overview.append(text)
        
        return overview.copy() as! NSAttributedString
    }
    
    var releaseDateText: String {
        let releaseYear = MovieDateUtils.year(from: movie)
        return "Year: \(releaseYear)"
    }
    
}

// MARK: Colors

extension MovieDetailViewModel {
    
    var backgroundColor: UIColor {
        return .black
    }
    
    var primaryTextColor: UIColor {
        return .white
    }
    
    var primaryDarkTextColor: UIColor {
        return .darkGray
    }
    
    var secondaryTextColor: UIColor {
        return .lightText
    }
    
}

// MARK: MovieDetailViewModel: ImagePresentable

extension MovieDetailViewModel: ImagePresentable {
    var imageUrl: URL {
        return TMDbWebservice.buildImageUrlFor(movie)
    }
}
