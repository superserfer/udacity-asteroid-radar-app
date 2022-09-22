package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import retrofit2.Call
import retrofit2.Response
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

// TODO: ADD your Token here
const val API_KEY = "DEMO_KEY"

class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidRepository(database)
    var asteroids = asteroidsRepository.asteroids

    private val _navigateToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid
        get() = _navigateToAsteroid

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay
        get() = _pictureOfTheDay

    init {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroid()
        }
        loadPictureOfTheDay()
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToAsteroid.value = null
    }

    fun onFilterChanged(filter: Filter) {
        asteroids = when(filter) {
            Filter.TODAY -> {
                asteroidsRepository.asteroidsToday
            }
            Filter.WEEK -> {
                asteroidsRepository.asteroidsWeek
            }
            else -> {
                asteroidsRepository.asteroids
            }
        }
    }

    private fun loadPictureOfTheDay() {
        NasaApi.retrofitService.getImageOfTheDay(API_KEY).enqueue( object: retrofit2.Callback<PictureOfDay> {
            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                _pictureOfTheDay.value = response.body()
            }

            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                println(t.message)
            }
        })
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

enum class Filter() {
    WEEK, TODAY, SAVED
}
