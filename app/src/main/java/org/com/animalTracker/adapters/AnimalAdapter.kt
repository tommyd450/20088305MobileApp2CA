package org.com.animalTracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.com.animalTracker.R
import org.com.animalTracker.databinding.CardAnimalBinding
import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.models.FireBaseImageManager
import org.com.animalTracker.models.FirebaseDBManager
import timber.log.Timber

interface AnimalClickListener {
    fun onAnimalClick(animal: AnimalModel)
}

class AnimalAdapter constructor(private var animals: List<AnimalModel>,
                                  private val listener: AnimalClickListener)
    : RecyclerView.Adapter<AnimalAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardAnimalBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    fun filterList(filterlist: ArrayList<AnimalModel>) {
        // below line is to add our filtered
        // list in our course array list.
        animals = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val animal = animals[holder.adapterPosition]
        holder.bind(animal,listener)
    }

    override fun getItemCount(): Int = animals.size

    inner class MainHolder(val binding : CardAnimalBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(animal: AnimalModel, listener: AnimalClickListener) {


            binding.speciesValue.text= animal.animalName.toString()
            binding.scNameValue.text = animal.animalSpecies.toString()
            binding.animalDiet.text = animal.diet.toString()
            binding.regionValue.text = animal.region.toString()
            if(animal.image != "null" && animal.image !="" )
            {
                Timber.i("THIS IS A TEST"+ animal.image)
                FireBaseImageManager.checkStorageForExistingImage(animal.uid)
                Timber.i("TESTURL"+FireBaseImageManager.objectImageUri.value.toString())
                Picasso.get().load(animal.image).into(binding.animPic)
            }




            binding.root.setOnClickListener { listener.onAnimalClick(animal) }
            binding.executePendingBindings()
        }
    }

    //override fun getFilter(){}
}