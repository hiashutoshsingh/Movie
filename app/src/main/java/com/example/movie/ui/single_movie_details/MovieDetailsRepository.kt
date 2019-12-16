package com.example.movie.ui.single_movie_details

import androidx.lifecycle.LiveData
import com.example.movie.data.api.MovieInterface
import com.example.movie.data.repository.MovieDetailNetworkSource
import com.example.movie.data.repository.NetworkState
import com.example.movie.model.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: MovieInterface) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailNetworkSource

    fun fetchSingleMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieDetails> {

        movieDetailsNetworkDataSource =
            MovieDetailNetworkSource(
                apiService,
                compositeDisposable
            )
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadMovieResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }

}