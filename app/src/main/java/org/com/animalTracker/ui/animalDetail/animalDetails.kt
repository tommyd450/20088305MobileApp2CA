package org.com.animalTracker.ui.animalDetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import org.com.animalTracker.databinding.FragmentAnimalDetailsBinding
import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.models.FireBaseImageManager
import org.com.animalTracker.ui.auth.LoggedInViewModel
import org.com.animalTracker.utils.json.showImagePicker
import timber.log.Timber


class AnimalDetails : Fragment() {
    private var _fragBinding: FragmentAnimalDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private lateinit var intentLauncher: ActivityResultLauncher<Intent>
    var animal = AnimalModel()
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
        Picasso.get().load(args.animal.image).into(fragBinding.animalDetailsImage)
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
        animal = args.animal
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                viewModel.liveFirebaseUser.value = firebaseUser

            }
        })
    }

    fun setButtonListener(layout: FragmentAnimalDetailsBinding)
    {
        registerImagePickerCallback()
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
                uid = args.animal.uid,
                image = args.animal.image
                )
            viewModel.updateAnimal(an)
            FireBaseImageManager.uploadObjectImageToFirebase(loggedInViewModel.liveFirebaseUser.value!!.uid,args.animal.uid,fragBinding.animalDetailsImage.drawable.toBitmap(),args.animal,true)
            val action = AnimalDetailsDirections.actionAnimalDetailsToNavGallery()
            findNavController().navigate(action)
        }
        layout.updateImage.setOnClickListener{
            showImagePicker(intentLauncher)
        }


    }

    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        if (result.data != null) {


                            val image = result.data!!.data!!
                            requireContext().contentResolver.takePersistableUriPermission(
                                image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                            //args.animal.image = image.toString()
                            Picasso.get().load(image).into(fragBinding.animalDetailsImage)




                        } // end of if
                    }
                    Activity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

}