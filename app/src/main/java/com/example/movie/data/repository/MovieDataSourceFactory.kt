package com.example.movie.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movie.model.Movie
import com.example.movie.data.api.MovieInterface
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(private val apiService: MovieInterface, private val compositeDisposable: CompositeDisposable):
    DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()


    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(
            apiService,
            compositeDisposable
        )

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}