package com.example.markersyandexmap.repository

import androidx.lifecycle.LiveData
import com.example.markersyandexmap.dto.Place

interface PlaceRepository {
    fun getAll(): LiveData<List<Place>>
    fun removeById(id: Int)
    fun save(place: Place)
}