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

// MARK: MoviesCollectionViewDataSource: NSObject

class MoviesCollectionViewDataSource: NSObject {
    
    var movies: [Movie]?
    var didSelect: ((Movie) -> ())? = { _ in }
    
    override init() {
        super.init()
    }
}

// MARK: MoviesCollectionViewDataSource: UICollectionViewDataSource

extension MoviesCollectionViewDataSource: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return movies?.count ?? 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView
            .dequeueReusableCell(withReuseIdentifier: MovieCollectionViewCell.reuseIdentifier,
                                 for: indexPath) as! MovieCollectionViewCell
        configure(cell: cell, atIndexPath: indexPath)
        return cell
    }
    
    private func configure(cell: MovieCollectionViewCell, atIndexPath indexPath: IndexPath) {
        let movie = movies![indexPath.row]
        weak var weakCell = cell
        TMDbWebservice.imageFor(movie: movie) { weakCell?.posterImageView.image = $0 }
    }
}

// MARK: - MoviesCollectionViewDataSource: UICollectionViewDelegateFlowLayout -

extension MoviesCollectionViewDataSource: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionView.bounds.width / 2,
                      height: collectionView.bounds.height / 2)
    }
}

// MARK: - MoviesCollectionViewDataSource: UICollectionViewDelegate -

extension MoviesCollectionViewDataSource: UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        didSelect?(movies![indexPath.row])
    }
}
