package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.main.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsteroidRepository(private val database: AsteroidsDatabase) {

    private val todayDate = LocalDateTime.now()
    private val weedEndDate = LocalDateTime.now().plusDays(7)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroid()) {
            it.asDomainModel()
        }

    val asteroidsToday: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidsToday(
            todayDate.format(formatter))) {
            it.asDomainModel()
        }

    val asteroidsWeek: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidsByRange(
            todayDate.format(formatter),
            weedEndDate.format(formatter))) {
            it.asDomainModel()
        }


    suspend fun refreshAsteroid() {
        withContext(Dispatchers.IO) {
           try {
               val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
               val today = formatter.format(LocalDateTime.now())
               val json = NasaApi.retrofitService.getAsteroid(API_KEY, today).await()
               val asteroids = parseAsteroidsJsonResult(JSONObject(json))
               database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
           } catch (ignored: Exception) {}
        }
    }
}
