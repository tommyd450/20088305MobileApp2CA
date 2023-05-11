package org.com.animalTracker.ui.AnimalList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

import org.com.animalTracker.models.AnimalModel

import org.com.animalTracker.models.FirebaseDBManager
import timber.log.Timber

class AnimalListViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()
    private val animalList = MutableLiveData<ArrayList<AnimalModel>>()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    val observableAnimalList: LiveData<ArrayList<AnimalModel>>
    get () = animalList

    var readOnly = MutableLiveData(false)

    init {
        load()
        readOnly.value = false
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
                Timber.i(e)
            }

    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findOverAll(animalList)
            Timber.i("Report LoadAll Success : ${animalList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }

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
    val text: LiveData<String> = _text
}