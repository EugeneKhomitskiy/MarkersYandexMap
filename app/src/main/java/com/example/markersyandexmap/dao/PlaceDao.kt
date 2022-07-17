package com.example.markersyandexmap.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.markersyandexmap.dto.Place
import com.example.markersyandexmap.entity.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM PlaceEntity ORDER BY title")
    fun getAll(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM PlaceEntity WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun getPlace(latitude: Double, longitude: Double): Place

    @Insert
    suspend fun insert(place: PlaceEntity)

    @Query("UPDATE PlaceEntity SET description = :description, title = :title WHERE id = :id")
    suspend fun update(id: Int, title: String, description: String)

    suspend fun save(place: PlaceEntity) =
        if (place.id == 0) insert(place) else update(place.id, place.title, place.description)

    @Query("DELETE FROM PlaceEntity WHERE id = :id")
    suspend fun removeById(id: Int)
}