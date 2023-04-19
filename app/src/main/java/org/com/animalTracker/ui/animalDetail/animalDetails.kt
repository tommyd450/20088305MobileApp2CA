package org.com.animalTracker.ui.animalDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.com.animalTracker.R
import org.com.animalTracker.databinding.FragmentAnimalDetailsBinding
import org.com.animalTracker.databinding.FragmentAnimallistBinding
import org.com.animalTracker.databinding.FragmentCreateanimalBinding
import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.models.FirebaseDBManager
import org.com.animalTracker.ui.AnimalList.AnimalListFragment
import org.com.animalTracker.ui.AnimalList.AnimalListFragmentDirections
import org.com.animalTracker.ui.auth.LoggedInViewModel
import org.com.animalTracker.ui.createAnimal.CreateAnimalViewModel
import timber.log.Timber

class AnimalDetails : Fragment() {
    private var _fragBinding: FragmentAnimalDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
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

        Timber.i("Args"+args.animal?.toString())
        _fragBinding = FragmentAnimalDetailsBinding.inflate(inflater, container, false)
        fragBinding.editNameField.setText(args.animal.animalName)
        fragBinding.editSpeciesField.setText(args.animal.animalSpecies)
        fragBinding.editRegionField.setText(args.animal.region)
        fragBinding.editDietField.setText(args.animal.diet)
        viewModel = ViewModelProvider(this).get(AnimalDetailsViewModel::class.java)
        Timber.i("PRESSED")
        print("Pressed")
        setButtonListener(fragBinding)

        return fragBinding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AnimalDetailsViewModel::class.java)

    }

    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                viewModel.liveFirebaseUser.value = firebaseUser

            }
        })
    }

    fun setButtonListener(layout: FragmentAnimalDetailsBinding)
    {
        var selected = MutableLiveData<AnimalModel>()
        selected.value = args.animal
        Timber.i("PRESSED")
        layout.confirmDelete.setOnClickListener{

            viewModel.removeAnimal(args.animal)
            val action = AnimalDetailsDirections.actionAnimalDetailsToNavGallery()
            findNavController().navigate(action)
        }
        layout.confirmUpdate.setOnClickListener{
            var an = AnimalModel(
                id = args.animal.id,
                animalName = layout.editNameField.text.toString(),
                animalSpecies = layout.editSpeciesField.text.toString(),
                region = layout.editRegionField.text.toString(),
                diet = layout.editDietField.text.toString(),
                uid = args.animal.uid)
            viewModel.updateAnimal(an )
            val action = AnimalDetailsDirections.actionAnimalDetailsToNavGallery()
            findNavController().navigate(action)
        }

    }

}