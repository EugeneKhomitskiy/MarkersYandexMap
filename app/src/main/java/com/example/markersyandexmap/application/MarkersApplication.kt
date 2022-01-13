package com.example.markersyandexmap.application

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class MarkersApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("Api key")
    }
}