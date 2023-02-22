package org.com.animalTracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.com.animalTracker.R
import org.com.animalTracker.databinding.CardAnimalBinding
import org.com.animalTracker.models.AnimalModel

interface AnimalClickListener {
    fun onDonationClick(animal: AnimalModel)
}

class AnimalAdapter constructor(private var animals: List<AnimalModel>,
                                  private val listener: AnimalClickListener)
    : RecyclerView.Adapter<AnimalAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardAnimalBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val donation = animals[holder.adapterPosition]
        holder.bind(donation,listener)
    }

    override fun getItemCount(): Int = animals.size

    inner class MainHolder(val binding : CardAnimalBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(animal: AnimalModel, listener: AnimalClickListener) {


            binding.speciesValue.text = animal.animalSpecies
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            binding.root.setOnClickListener { listener.onDonationClick(animal) }
            binding.executePendingBindings()
        }
    }
}