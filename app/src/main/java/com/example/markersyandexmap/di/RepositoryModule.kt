package com.example.markersyandexmap.di

import com.example.markersyandexmap.repository.PlaceRepository
import com.example.markersyandexmap.repository.PlaceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindsPlaceService(impl: PlaceRepositoryImpl): PlaceRepository
}