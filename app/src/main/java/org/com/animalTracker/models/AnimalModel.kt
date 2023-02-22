package org.com.animalTracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimalModel(var id: Long = 0,
                         val animalSpecies: String = "N/A",
                         val animalName : String = "",
                         val region: String = "Earth") : Parcelable