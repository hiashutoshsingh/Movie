package com.example.movie.ui.popular_movie

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movie.R
import com.example.movie.data.api.MovieClient
import com.example.movie.data.api.MovieInterface
import com.example.movie.data.repository.NetworkState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    lateinit var movieRepository: MoviePagedListRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService : MovieInterface = MovieClient.getClient()
        movieRepository =
            MoviePagedListRepository(
                apiService
            )
            viewModel = getViewModlel()
        val movieAdapter =
            PopularMoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this,2)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType==movieAdapter.MOVIE_VIEW_TYPE) return 1
                else return 3
            }
        }


        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility=if (viewModel.listIsEmpty()&&it== NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility   = if (viewModel.listIsEmpty()&&it== NetworkState.ERROR) View.VISIBLE else View.GONE

            if(!viewModel.listIsEmpty()){
                movieAdapter.setNetworkState(it)
            }
        })

    }

    private fun getViewModlel(): MainActivityViewModel {
        return ViewModelProviders.of(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainActivityViewModel(
                    movieRepository
                ) as T
            }
        })[MainActivityViewModel::class.java]
    }

}
