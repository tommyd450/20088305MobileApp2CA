package org.com.animalTracker.models

interface AnimalStoreInterface {

    fun findAll() : List<AnimalModel>
    fun findById(id:Long) : AnimalModel?
    fun create(animal : AnimalModel)
    fun delete(animal: AnimalModel)
    fun update(animal: AnimalModel)
}