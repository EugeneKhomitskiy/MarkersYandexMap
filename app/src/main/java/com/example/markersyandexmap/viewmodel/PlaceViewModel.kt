package com.example.markersyandexmap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.markersyandexmap.db.AppDb
import com.example.markersyandexmap.dto.Place
import com.example.markersyandexmap.repository.PlaceRepository
import com.example.markersyandexmap.repository.PlaceRepositoryImpl

private val empty = Place(
    id = 0,
    title = "",
    description = "",
    latitude = 0.0,
    longitude = 0.0
)

class PlaceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlaceRepository = PlaceRepositoryImpl(AppDb.getInstance(context = application).placeDao)

    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun save(latitude: Double, longitude: Double) {
        edited.value?.let {
            repository.save(it.copy(
                latitude = latitude,
                longitude = longitude
            ))
        }
        edited.value = empty
    }

    fun edit(place: Place) {
        edited.value = place
    }

    fun changeTitle(title: String) {
        edited.value?.let {
            val titleText = title.trim()
            if (it.title != titleText) {
                edited.value = it.copy(
                    title = titleText
                )
            }
        }
    }

    fun changeDescription(description: String) {
        edited.value?.let {
            val descriptionText = description.trim()
            if (it.description != descriptionText) {
                edited.value = it.copy(
                    description = descriptionText
                )
            }
        }
    }

    fun removeById(id: Int) {
        repository.removeById(id)
    }
}