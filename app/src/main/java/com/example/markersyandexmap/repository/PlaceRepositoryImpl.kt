package com.example.markersyandexmap.repository

import com.example.markersyandexmap.dao.PlaceDao
import com.example.markersyandexmap.dto.Place
import com.example.markersyandexmap.entity.PlaceEntity
import com.example.markersyandexmap.entity.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeDao: PlaceDao
): PlaceRepository {

    override val data = placeDao.getAll()
        .map(List<PlaceEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun removeById(id: Int) {
        placeDao.removeById(id)
    }

    override suspend fun save(place: Place) {
        placeDao.save(
            PlaceEntity.fromDto(place)
        )
    }
}