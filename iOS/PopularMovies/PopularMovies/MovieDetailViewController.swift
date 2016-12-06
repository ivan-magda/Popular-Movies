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

// MARK: MovieDetailViewController: UIViewController

final class MovieDetailViewController: UIViewController {
    
    // MARK: IBOutlets
    
    @IBOutlet weak var contentView: MovieDetailScrollView!
    
    // MARK: Properties
    
    var movie: Movie!

    // MARK: View Lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
    }
    
    // MARK: Private
    
    private func setupUI() {
        contentView.titleLabel.text = movie.title
        contentView.ratingLabel.text = "Rating: \(movie.rating)"
        contentView.overviewLabel.text = movie.overview
        
        let releaseYear = MovieDateUtils.year(from: movie)
        contentView.releaseDateLabel.text = "Year: \(releaseYear)"
        
        let url = TMDbWebservice.buildImageUrlFor(movie)
        contentView.posterImageView.af_setImage(withURL: url)
    }

}
