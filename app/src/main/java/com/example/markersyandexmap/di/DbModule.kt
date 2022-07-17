package com.example.markersyandexmap.di

import android.content.Context
import androidx.room.Room
import com.example.markersyandexmap.dao.PlaceDao
import com.example.markersyandexmap.db.AppDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DbModule {
    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDb =
        Room.databaseBuilder(context, AppDb::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesPostDao(appDb: AppDb): PlaceDao = appDb.placeDao
}