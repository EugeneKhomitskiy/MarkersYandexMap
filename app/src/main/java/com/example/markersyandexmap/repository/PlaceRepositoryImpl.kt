package com.example.markersyandexmap.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.markersyandexmap.dao.PlaceDao
import com.example.markersyandexmap.dto.Place
import com.example.markersyandexmap.entity.PlaceEntity

class PlaceRepositoryImpl(private val placeDao: PlaceDao): PlaceRepository {

    override fun getAll(): LiveData<List<Place>> = Transformations.map(placeDao.getAll()) { list ->
        list.map { it.toDto() }
    }

    override fun removeById(id: Int) {
        placeDao.removeById(id)
    }

    override fun save(place: Place) {
        placeDao.save(
            PlaceEntity.fromDto(place)
        )
    }
}