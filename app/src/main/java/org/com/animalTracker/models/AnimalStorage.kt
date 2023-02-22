package org.com.animalTracker.models

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class AnimalStorage : AnimalStoreInterface {

    val animals = ArrayList<AnimalModel>()

    override fun findAll(): List<AnimalModel> {
        return animals
    }

    override fun findById(id:Long) : AnimalModel? {
        val foundAnimal: AnimalModel? = animals.find { it.id == id }
        return foundAnimal
    }

    override fun create(animal: AnimalModel) {
        animal.id = getId()
        animals.add(animal)
        logAll()
    }

    fun logAll() {
        Timber.v("** Donations List **")
        animals.forEach { Timber.v("Donate ${it}") }
    }
}

class AnimalModell {

}
