package org.com.animalTracker.ui.AnimalList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

import org.com.animalTracker.models.AnimalModel

import org.com.animalTracker.models.FirebaseDBManager

class AnimalListViewModel : ViewModel() {


    private val animalList = MutableLiveData<List<AnimalModel>>()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    val observableAnimalList: LiveData<List<AnimalModel>>
    get () = animalList

    init {
        load()
    }

    fun load()
    {
        //animalList.value = AnimalJSONStore.findAll()
        try
        {
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,animalList)
        }
        catch(e: java.lang.Exception)
            {

            }

    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}