package com.example.markersyandexmap.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.markersyandexmap.dao.PlaceDao
import com.example.markersyandexmap.entity.PlaceEntity

@Database(entities = [PlaceEntity::class], version = 1)
abstract class AppDb: RoomDatabase() {
    abstract val placeDao: PlaceDao
}