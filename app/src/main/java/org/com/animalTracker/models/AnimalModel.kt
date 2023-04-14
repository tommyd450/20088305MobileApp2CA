package org.com.animalTracker.models

import android.os.Parcelable
import com.google.firebase.database.Exclude

import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimalModel(var id: Long = 0,
                       var animalSpecies: String = "N/A",
                       var animalName : String = "",
                       var region: String = "Earth",
                       var diet : String = "",
                       var uid: String = "",
                       var email : String ="") : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "animalSpecies" to animalSpecies,
            "animalName" to animalName,
            "region" to region,
            "diet" to diet,
            "uid" to uid,
            "email" to email
        )
    }
}