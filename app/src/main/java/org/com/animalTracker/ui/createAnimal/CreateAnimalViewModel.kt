package org.com.animalTracker.ui.createAnimal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser

import org.com.animalTracker.models.AnimalModel

import org.com.animalTracker.models.FirebaseDBManager
import org.com.animalTracker.ui.auth.LoggedInViewModel

class CreateAnimalViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }

    val text: LiveData<String> = _text

    fun addAnimal(firebaseUser: MutableLiveData<FirebaseUser>,animal: AnimalModel)
    {

        status.value = try {
            //AnimalJSONStore.create(animal)
            FirebaseDBManager.create(firebaseUser,animal)
            true
        } catch (e:IllegalArgumentException){
            false
        }

    }

    fun selectImage()
    {


    }
}