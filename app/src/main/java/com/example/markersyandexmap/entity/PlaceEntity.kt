package com.example.markersyandexmap.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.markersyandexmap.dto.Place

@Entity
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
) {

    fun toDto() = Place(
        id,
        title,
        description,
        latitude,
        longitude
    )

    companion object {
        fun fromDto(place: Place) =
            PlaceEntity(
                place.id,
                place.title,
                place.description,
                place.latitude,
                place.longitude
            )
    }
}

fun List<PlaceEntity>.toDto() = map(PlaceEntity::toDto)