package org.com.animalTracker.ui.animalDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.com.animalTracker.R
import org.com.animalTracker.databinding.FragmentAnimalDetailsBinding
import org.com.animalTracker.databinding.FragmentAnimallistBinding
import org.com.animalTracker.databinding.FragmentCreateanimalBinding
import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.ui.AnimalList.AnimalListFragment
import org.com.animalTracker.ui.AnimalList.AnimalListFragmentDirections
import org.com.animalTracker.ui.createAnimal.CreateAnimalViewModel
import timber.log.Timber

class AnimalDetails : Fragment() {
    private var _fragBinding: FragmentAnimalDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!
    companion object {
        fun newInstance() = AnimalDetails()
    }

    private lateinit var viewModel: AnimalDetailsViewModel
    //private val args by navArgs<AnimalDetailsArgs>()
    val args: AnimalDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*Timber.i("Args"+AnimalJSONStore.findById(args.animal)?.toString())
        _fragBinding = FragmentAnimalDetailsBinding.inflate(inflater, container, false)
        fragBinding.editNameField.setText(AnimalJSONStore.findById(args.animal)?.animalName)
        fragBinding.editSpeciesField.setText(AnimalJSONStore.findById(args.animal)?.animalSpecies)
        fragBinding.editRegionField.setText(AnimalJSONStore.findById(args.animal)?.region)
        fragBinding.editDietField.setText(AnimalJSONStore.findById(args.animal)?.diet)
        viewModel = ViewModelProvider(this).get(AnimalDetailsViewModel::class.java)*/
        Timber.i("PRESSED")
        print("Pressed")
        setButtonListener(fragBinding)

        return fragBinding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AnimalDetailsViewModel::class.java)

    }

    fun setButtonListener(layout: FragmentAnimalDetailsBinding)
    {
        Timber.i("PRESSED")
        /*layout.confirmDelete.setOnClickListener{
            var an: AnimalModel? = AnimalJSONStore.findById(args.animal)
            viewModel.removeAnimal(an as AnimalModel)
            val action = AnimalDetailsDirections.actionAnimalDetailsToNavGallery()
            findNavController().navigate(action)
        }*/
        layout.confirmUpdate.setOnClickListener{
            var an = AnimalModel(
                id = args.animal,
                animalName = layout.editNameField.text.toString(),
                animalSpecies = layout.editSpeciesField.text.toString(),
                region = layout.editRegionField.text.toString(),
                diet = layout.editDietField.text.toString())
            viewModel.updateAnimal(an )
            val action = AnimalDetailsDirections.actionAnimalDetailsToNavGallery()
            findNavController().navigate(action)
        }

    }

}