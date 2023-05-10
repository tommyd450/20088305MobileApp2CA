package org.com.animalTracker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.models.FirebaseDBManager
import timber.log.Timber


class QuickAddViewModel : ViewModel() {


    private val animalList = MutableLiveData<ArrayList<AnimalModel>>()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    val observableAnimalList: LiveData<ArrayList<AnimalModel>>
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
    fun addAnimal(firebaseUser: MutableLiveData<FirebaseUser>,animal: AnimalModel)
    {
        status.value = try {

            FirebaseDBManager.create(firebaseUser,animal)

            true
        } catch (e:IllegalArgumentException){
            false
        }

    }
}