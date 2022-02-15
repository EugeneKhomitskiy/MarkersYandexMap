package com.example.markersyandexmap.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.markersyandexmap.entity.PlaceEntity

@Dao
interface PlaceDao {
    @Query("SELECT * FROM PlaceEntity ORDER BY title")
    fun getAll(): LiveData<List<PlaceEntity>>

    @Insert
    fun insert(place: PlaceEntity)

    @Query("UPDATE PlaceEntity SET description = :description, title = :title WHERE id = :id")
    fun update(id: Int, title: String, description: String)

    fun save(place: PlaceEntity) =
        if (place.id == 0) insert(place) else update(place.id, place.title, place.description)

    @Query("DELETE FROM PlaceEntity WHERE id = :id")
    fun removeById(id: Int)
}