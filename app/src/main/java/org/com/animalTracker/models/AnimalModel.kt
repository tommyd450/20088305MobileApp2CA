package org.com.animalTracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimalModel(var id: Long = 0,
                       var animalSpecies: String = "N/A",
                       var animalName : String = "",
                       var region: String = "Earth",
                       var diet : String = "") : Parcelable