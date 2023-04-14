package org.com.animalTracker.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import timber.log.Timber

object FirebaseDBManager:AnimalStoreInterface {
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    override fun findAll(donationsList: MutableLiveData<List<AnimalModel>>) {
        TODO("Not yet implemented")
    }


    override fun findAll(userid: String, animalsList: MutableLiveData<List<AnimalModel>>) {
        database.child("user-animals").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Donation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<AnimalModel>()
                    val children = snapshot.children
                    children.forEach {
                        val animal = it.getValue(AnimalModel::class.java)
                        localList.add(animal!!)
                    }
                    database.child("user-donations").child(userid)
                        .removeEventListener(this)

                    animalsList.value = localList
                }
            })
    }

    override fun findById(userid: String, donationid: String, donation: MutableLiveData<AnimalModel>) {
     TODO("Not yet implemented")
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, animal: AnimalModel) {
     Timber.i("Firebase DB Reference: $database")

     val uid = firebaseUser.value!!.uid
     val key = database.child("animals").push().key
     if(key == null)
     {
      Timber.i("Firebase Error : Key Empty")
      return
     }
     animal.uid = key
     val animalValues = animal.toMap()
     val childAdd = HashMap<String,Any>()
     childAdd["/animals/$key"] = animalValues
     childAdd["/user-animals/$uid/$key"] = animalValues

     database.updateChildren(childAdd)
    }

    override fun delete(userid: String, animalid: String) {
     TODO("Not yet implemented")
    }

    override fun update(userid: String, animalid: String, animal: AnimalModel) {
     TODO("Not yet implemented")
    }
}