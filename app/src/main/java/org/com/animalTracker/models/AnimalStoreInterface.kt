package org.com.animalTracker.models

import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface AnimalStoreInterface {

    /*fun findAll() : List<AnimalModel>
    fun findById(id:Long) : AnimalModel?
    fun create(animal : AnimalModel)
    fun delete(animal: AnimalModel)

    fun update(animal: AnimalModel)*/



        fun findAll(userid:String,
                    donationsList:
                    MutableLiveData<List<AnimalModel>>)
        fun findById(userid:String, donationid: String,
                     donation: MutableLiveData<AnimalModel>)
        fun create(firebaseUser: MutableLiveData<FirebaseUser>, animal: AnimalModel)
        fun delete(userid:String, animalid: String)
        fun update(userid:String, animalid: String, animal: AnimalModel)

}