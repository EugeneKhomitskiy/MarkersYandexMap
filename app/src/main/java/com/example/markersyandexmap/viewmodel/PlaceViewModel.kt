package com.example.markersyandexmap.viewmodel

import androidx.lifecycle.*
import com.example.markersyandexmap.dto.Place
import com.example.markersyandexmap.repository.PlaceRepository
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private val empty = Place(
    id = 0,
    title = "",
    description = "",
    latitude = 0.0,
    longitude = 0.0
)

@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val repository: PlaceRepository
): ViewModel() {

    val data: LiveData<List<Place>> = repository.data.asLiveData(Dispatchers.Default)
    val edited = MutableLiveData(empty)
    val place = MutableLiveData<Place>()
    val currentPosition = MutableLiveData<Point?>()

    fun save(latitude: Double, longitude: Double) = viewModelScope.launch {
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

    fun removeById(id: Int) = viewModelScope.launch {
        repository.removeById(id)
    }

    fun getPlace(latitude: Double, longitude: Double) = viewModelScope.launch {
        place.value = repository.getPlace(latitude, longitude)
    }

    fun savePosition(point: Point?) {
        currentPosition.value = point
    }
}