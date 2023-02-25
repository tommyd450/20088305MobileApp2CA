package org.com.animalTracker.ui.createAnimal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.com.animalTracker.models.AnimalJSONStore
import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.models.AnimalStorage

class CreateAnimalViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }

    val text: LiveData<String> = _text

    fun addAnimal(animal: AnimalModel)
    {
        status.value = try {
            AnimalJSONStore.create(animal)
            true
        } catch (e:IllegalArgumentException){
            false
        }

    }
}