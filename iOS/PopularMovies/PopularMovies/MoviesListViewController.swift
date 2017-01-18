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

fileprivate enum SegueIdentifier: String {
  case movieDetail = "ShowMovieDetail"
}

// MARK: ViewController: UIViewController

final class MoviesListViewController: UIViewController {
  
  // MARK: IBOutlets
  
  @IBOutlet weak var collectionView: UICollectionView!
  @IBOutlet weak var sortingOptionsButton: UIBarButtonItem!
  
  // MARK: Properties
  
  var tmdbWebService: TMDbWebservice!
  var movieRealmManager: MovieRealmManager!
  
  fileprivate var sortOrder = MovieCollections.MovieTypes.popular
  fileprivate let collectionViewDataSource = MoviesCollectionViewDataSource()
  
  fileprivate let refreshControl = UIRefreshControl()
  
  // MARK: View Lifecycle
  
  override func viewDidLoad() {
    super.viewDidLoad()
    assert(tmdbWebService != nil && movieRealmManager != nil)
    
    configure()
    getPersisted()
  }
  
  override func viewWillLayoutSubviews() {
    super.viewWillLayoutSubviews()
    collectionView.collectionViewLayout.invalidateLayout()
  }
  
  override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
    if segue.identifier == SegueIdentifier.movieDetail.rawValue {
      let selectedMovie = sender as! Movie
      let detailVC = segue.destination as! MovieDetailViewController
      detailVC.movie = selectedMovie
    }
  }
  
  // MARK: Private
  
  private func configure() {
    collectionView.dataSource = collectionViewDataSource
    collectionView.delegate = collectionViewDataSource
    collectionView.alwaysBounceVertical = true
    
    weak var weakSelf = self
    collectionViewDataSource.didSelect = weakSelf?.showDetail
    
    sortingOptionsButton.target = self
    sortingOptionsButton.action = #selector(changeSortOrderButtonDidPressed)
    
    refreshControl.addTarget(self, action: #selector(loadData), for: .valueChanged)
    refreshControl.tintColor = .white
    collectionView.addSubview(refreshControl)
    
    updateTitle()
    navigationItem.backBarButtonItem = UIBarButtonItem(title:"", style:.plain, target:nil, action:nil)
  }
  
  private func showDetail(_ movie: Movie) {
    performSegue(withIdentifier: SegueIdentifier.movieDetail.rawValue, sender: movie)
  }
  
}

// MARK: - Working with Data Source -

extension MoviesListViewController {
  
  fileprivate func getPersisted() {
    let movieCollection = movieRealmManager.collections()
    let movies = Array(movieCollection.getCollection(for: sortOrder))
    updateDataSource(movies)
    
    if movies.isEmpty {
      loadData()
    }
  }
  
  @objc fileprivate func loadData() {
    if !refreshControl.isRefreshing {
      startRefreshing()
    }
    
    weak var weakSelf = self
    switch sortOrder {
    case .popular:
      weakSelf?.tmdbWebService.popularMovies(success: persistMovies, failure: failedLoad)
    case .topRated:
      weakSelf?.tmdbWebService.topRatedMovies(success: persistMovies, failure: failedLoad)
      break
    }
  }
  
  private func persistMovies(_ movies: [Movie]) {
    refreshControl.endRefreshing()
    movieRealmManager.collections().persist(movies, forType: sortOrder)
    updateDataSource(movies)
  }
  
  fileprivate func updateDataSource(_ newMovies: [Movie]) {
    collectionViewDataSource.movies = newMovies
    collectionView.reloadData()
  }
  
}

// MARK: - Actions -

extension MoviesListViewController {
  @objc fileprivate func changeSortOrderButtonDidPressed() {
    func processOnSelected(order newSortOrder: MovieCollections.MovieTypes) {
      guard sortOrder != newSortOrder else { return }
      sortOrder = newSortOrder
      updateTitle()
      updateDataSource([])
      getPersisted()
    }
    
    let actionSheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
    actionSheet.view.tintColor = .black
    actionSheet.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
    
    let sortByPopularity = UIAlertAction(title: "\(MovieCollections.MovieTypes.popular)", style: .default) { action in
      processOnSelected(order: .popular)
    }
    
    let sortByRating = UIAlertAction(title: "\(MovieCollections.MovieTypes.topRated)", style: .default) { action in
      processOnSelected(order: .topRated)
    }
    
    actionSheet.addAction(sortByPopularity)
    actionSheet.addAction(sortByRating)
    
    present(actionSheet, animated: true, completion: nil)
  }
}

// MARK: - UI Fucntions -

extension MoviesListViewController {
  
  fileprivate func startRefreshing() {
    collectionView.setContentOffset(CGPoint(x: 0, y: -refreshControl.frame.size.height*2), animated: true)
    refreshControl.beginRefreshing()
  }
  
  fileprivate func updateTitle() {
    navigationItem.title = "\(sortOrder) Movies"
  }
  
  fileprivate func failedLoad(with error: Error) {
    refreshControl.endRefreshing()
    presentErrorAlert(with: error)
  }
  
  fileprivate func presentErrorAlert(with error: Error) {
    let alert = UIAlertController(title: "An error occured", message: "\(error)", preferredStyle: .alert)
    alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: nil))
    present(alert, animated: true, completion: nil)
  }
  
}
