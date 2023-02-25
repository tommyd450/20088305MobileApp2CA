package org.com.animalTracker.main

import android.app.Application
import org.com.animalTracker.models.AnimalJSONStore
import org.com.animalTracker.models.AnimalStorage
import org.com.animalTracker.models.AnimalStoreInterface
import timber.log.Timber

class App: Application() {
    lateinit var animalStorage: AnimalStoreInterface

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        //animalStorage = AnimalJSONStore
        Timber.i("Animal Tracker Application Started")
    }
}