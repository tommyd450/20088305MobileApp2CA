package org.com.animalTracker.models

interface AnimalStoreInterface {

    fun findAll() : List<AnimalModel>
    fun findById(id:Long) : AnimalModel?
    fun create(donation : AnimalModel)
}