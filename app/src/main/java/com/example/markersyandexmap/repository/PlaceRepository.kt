package com.example.markersyandexmap.repository

import com.example.markersyandexmap.dto.Place
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    val data: Flow<List<Place>>
    suspend fun removeById(id: Int)
    suspend fun save(place: Place)
    suspend fun getPlace(latitude: Double, longitude: Double): Place
}