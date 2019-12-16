package com.example.movie.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movie.data.api.MovieInterface
import com.example.movie.data.api.POST_PER_PAGE
import com.example.movie.data.repository.MovieDataSource
import com.example.movie.data.repository.MovieDataSourceFactory
import com.example.movie.data.repository.NetworkState
import com.example.movie.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository(private val apiService: MovieInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        movieDataSourceFactory =
            MovieDataSourceFactory(
                apiService,
                compositeDisposable
            )

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(movieDataSourceFactory,config).build()
        return  moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.moviesLiveDataSource,
            MovieDataSource::networkState
        )
    }

}