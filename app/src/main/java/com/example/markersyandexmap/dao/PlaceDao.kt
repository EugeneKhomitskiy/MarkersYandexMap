package com.example.markersyandexmap.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.markersyandexmap.entity.PlaceEntity

@Dao
interface PlaceDao {
    @Query("SELECT * FROM PlaceEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PlaceEntity>>

    @Insert
    fun insert(place: PlaceEntity)

    @Query("UPDATE PlaceEntity SET description = :description WHERE id = :id")
    fun updateDescription(id: Int, description: String)

    fun save(place: PlaceEntity) =
        if (place.id == 0) insert(place) else updateDescription(place.id, place.description)

    @Query("DELETE FROM PlaceEntity WHERE id = :id")
    fun removeById(id: Int)
}