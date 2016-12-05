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

fileprivate enum SortOrder {
    case popular, topRated
}

// MARK: ViewController: UIViewController

class MoviesListViewController: UIViewController {
    
    // MARK: IBOutlets
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    // MARK: Properties
    
    var tmdbWebService: TMDbWebservice!
    
    fileprivate var sortOrder = SortOrder.popular
    fileprivate let collectionViewDataSource = MoviesCollectionViewDataSource()
    
    // MARK: View Lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        assert(tmdbWebService != nil)
        
        configure()
        loadData()
    }
    
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        collectionView.collectionViewLayout.invalidateLayout()
    }
    
    // MARK: Configure
    
    private func configure() {
        collectionView.dataSource = collectionViewDataSource
        collectionView.delegate = collectionViewDataSource
        
        weak var weakSelf = self
        collectionViewDataSource.didSelect = weakSelf?.showDetail
    }
    
    private func showDetail(_ movie: Movie) {
        
    }
}

// MARK: - Working with Data Source -

extension MoviesListViewController {
    
    fileprivate func loadData() {
        weak var weakSelf = self
        switch sortOrder {
        case .popular:
            weakSelf?.tmdbWebService.popularMovies(success: updateDataSource, failure: presentAlertWith)
        case .topRated:
            weakSelf?.tmdbWebService.topRatedMovies(success: updateDataSource, failure: presentAlertWith)
            break
        }
    }
    
    private func updateDataSource(_ newMovies: [Movie]) {
        collectionViewDataSource.movies = newMovies
        collectionView.reloadSections(IndexSet(integer: 0))
    }
    
}

// MARK: - UI Fucntions -

extension MoviesListViewController {
    fileprivate func presentAlertWith(error: Error) {
        let alert = UIAlertController(title: "An error occured", message: "\(error)", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: nil))
        present(alert, animated: true, completion: nil)
    }
}
