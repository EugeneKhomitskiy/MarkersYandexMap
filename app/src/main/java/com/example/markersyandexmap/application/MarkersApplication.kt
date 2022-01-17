package com.example.markersyandexmap.application

import android.app.Application
import com.example.markersyandexmap.BuildConfig
import com.yandex.mapkit.MapKitFactory

class MarkersApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAPS_API_KEY)
    }
}