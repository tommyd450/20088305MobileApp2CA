package org.com.animalTracker.ui.AnimalList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.models.AnimalStorage

class AnimalListViewModel : ViewModel() {


    private val animalList = MutableLiveData<List<AnimalModel>>()

    val observableAnimalList: LiveData<List<AnimalModel>>
    get () = animalList

    init {
        load()
    }

    fun load()
    {
        animalList.value = AnimalStorage.findAll()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}