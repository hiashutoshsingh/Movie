package com.example.movie.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movie.model.MovieDetails
import com.example.movie.data.api.MovieInterface
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailNetworkSource(
    private val apiService: MovieInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState


    private val _downloadMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val downloadMovieResponse: LiveData<MovieDetails>
        get() = _downloadMovieDetailsResponse

    fun fetchMovieDetails(movieID: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieID)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        }, {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsData", it.message)
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("MovieDetailsData", e.message)

        }
    }
}