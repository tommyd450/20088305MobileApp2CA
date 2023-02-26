package org.com.animalTracker.ui.animalDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.com.animalTracker.models.AnimalJSONStore
import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.models.AnimalStorage

class AnimalDetailsViewModel : ViewModel() {
    private val status = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = status
    fun removeAnimal(animal: AnimalModel)
    {
        status.value = try {
            AnimalJSONStore.delete(animal)
            true
        } catch (e:IllegalArgumentException){
            false
        }

    }

    fun updateAnimal(animal: AnimalModel)
    {
        status.value = try{
            AnimalJSONStore.update(animal)
            true
        } catch (e:IllegalArgumentException){
        false
    }
    }
}