package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _navigateToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid
        get() = _navigateToAsteroid

    init {
        // TODO: Implement API connection/DB buffer and remove test data
        _asteroids.value = listOf<Asteroid>(
            Asteroid(1L, "(2005 SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, false),
            Asteroid(2L, "(2006 SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, true),
            Asteroid(3L, "(20zrz05 SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, false),
            Asteroid(4L, "(200345 SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, false),
            Asteroid(5L, "(ftt SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, false),
            Asteroid(6L, "(2ff0343405 SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, false),
            Asteroid(7L, "(2005 SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, false),
            Asteroid(8L, "(200re5 SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, true),
            Asteroid(9L, "(20f05 SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, false),
            Asteroid(10L, "(2fd005 SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, false),
            Asteroid(11L, "(20dfg05 SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, false),
            Asteroid(12L, "(20fg05 SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, false),
            Asteroid(13L, "(2005jhk SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, false),
            Asteroid(14L, "(2005hgf SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, true),
            Asteroid(15L, "(20fdfdfdfdfd05 SC)", "2022-09-15", 20.2, 0.5420507863, 24.985643653, 0.3241439714, false)
            )
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToAsteroid.value = null
    }

}