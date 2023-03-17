package com.example.testyandexmap

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App : Application() {
    override fun onCreate() {
        MapKitFactory.setApiKey("84d57f4c-95cc-4ea2-bc43-037ccc559c0a")
        super.onCreate()
    }
}