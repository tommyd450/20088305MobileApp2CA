package org.com.animalTracker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.com.animalTracker.models.AnimalModel


class QuickAddViewModel : ViewModel() {


    private val animalList = MutableLiveData<List<AnimalModel>>()

    val observableAnimalList: LiveData<List<AnimalModel>>
        get () = animalList

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    fun load()
    {
        //animalList.value = TempStore.findAll()
    }
    val text: LiveData<String> = _text
    fun addAnimal(animal: AnimalModel)
    {
        status.value = try {
            //TempStore.create(animal)
            true
        } catch (e:IllegalArgumentException){
            false
        }

    }
}