package org.com.animalTracker.ui.animalDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.models.FirebaseDBManager
import timber.log.Timber


class AnimalDetailsViewModel : ViewModel() {
    private val status = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = status

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    fun removeAnimal(animal: AnimalModel)
    {
        status.value = try {
            //AnimalJSONStore.delete(animal)
            Timber.i("Animal Details"+animal.uid+""+animal.id)
            FirebaseDBManager.delete(liveFirebaseUser.value!!.uid,""+animal.uid)
            Timber.i("Completed")
            true
        } catch (e:java.lang.Exception){
            Timber.i("Failed"+e)
            false
        }

    }

    fun updateAnimal(animal: AnimalModel)
    {
        try{

            FirebaseDBManager.update(liveFirebaseUser.value!!.uid, animal.uid,animal)
        } catch (e:IllegalArgumentException){
        false
    }
    }
}