package org.com.animalTracker.models

import org.com.animalTracker.firebase.FirebaseAuthManager
import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}



object TempStore
{
    val tanimals = ArrayList<AnimalModel>()
    fun findAll(): List<AnimalModel> {
        return tanimals
    }

    fun findById(id:Long) : AnimalModel? {
        val foundAnimal: AnimalModel? = tanimals.find { it.id == id }
        return foundAnimal
    }

    fun create(animal: AnimalModel) {
        animal.id = getId()
        tanimals.add(animal)
        Timber.i("TEST")
        logAll()
    }

    fun logAll() {

        tanimals.forEach { Timber.v("Animal ${it}") }
    }

    fun delete(animal: AnimalModel) {
        tanimals.remove(animal)
    }

    fun update(animal: AnimalModel) {
        var found: AnimalModel? = tanimals.find { p -> p.id == animal.id }
        if (found != null) {
            Timber.i("Updated")
            found.animalName = animal.animalName
            found.animalSpecies = animal.animalSpecies
            found.region = animal.region
            logAll()
        }
    }
}