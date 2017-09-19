# PopularMovies

[![codebeat badge](https://codebeat.co/badges/3311300e-7131-4915-957b-d77f7a87b884)](https://codebeat.co/projects/github-com-vanyaland-popular-movies-master)

## Description
This application help users discover popular and recent movies using [TMDb API](https://www.themoviedb.org/documentation/api).

### Android Installation
- Obtain an [TMDb API](https://www.themoviedb.org/documentation/api) Key.
- In `TMDbApi.java`, replace `API_KEY` constant with your own.
- Build & run, enjoy.


### iOS Installation
- Obtain an [TMDb API](https://www.themoviedb.org/documentation/api) Key.
- Run `pod install` on iOS project root directory `.../PopularMovies/iOS/PopularMovies` ([CocoaPods Installation](https://guides.cocoapods.org/using/getting-started.html)).
- Open `PopularMovies.xcworkspace` and build.
- In `TMDbConstants.swift`, replace `apiKey` with your own in the `TMDbConstants.TMDBParameterValues` struct.
- Build & run, enjoy.

## Screenshots

<img src="https://github.com/vanyaland/Popular-Movies/blob/master/res/iOS-movies-list.png">
<img src="https://github.com/vanyaland/Popular-Movies/blob/master/res/iOS-movie-detail.png">


<img src="https://github.com/vanyaland/Popular-Movies/blob/master/res/Android-top-rated-movies.png">
<img src="https://github.com/vanyaland/Popular-Movies/blob/master/res/Android-movie-detail.png">
