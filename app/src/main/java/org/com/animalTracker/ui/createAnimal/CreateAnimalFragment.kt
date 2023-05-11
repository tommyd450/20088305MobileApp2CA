package org.com.animalTracker.ui.createAnimal

import android.app.Activity.*
import android.content.Intent
import android.content.ContentResolver
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.com.animalTracker.R
import org.com.animalTracker.activities.MainActivity
import org.com.animalTracker.databinding.FragmentCreateanimalBinding
import org.com.animalTracker.main.App
import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.models.FireBaseImageManager
import org.com.animalTracker.models.FirebaseDBManager
import org.com.animalTracker.ui.AnimalList.AnimalListFragmentDirections
import org.com.animalTracker.ui.auth.LoggedInViewModel
import org.com.animalTracker.ui.maps.MapsViewModel
import org.com.animalTracker.utils.json.readImageUri
import org.com.animalTracker.utils.json.showImagePicker
import timber.log.Timber


class CreateAnimalFragment : Fragment() {
    lateinit var app: App
    private var _binding: FragmentCreateanimalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var createAnimalViewModel: CreateAnimalViewModel
    private var mRequestQueue: RequestQueue? = null
    private var mStringRequest: StringRequest? = null
    private lateinit var loggedInViewModel: LoggedInViewModel
    private val url = "https://api.api-ninjas.com/v1/animals?name="
    private lateinit var intentLauncher: ActivityResultLauncher<Intent>
    private val mapsViewModel: MapsViewModel by activityViewModels()

    var animal = AnimalModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRequestQueue = Volley.newRequestQueue(activity)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentCreateanimalBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //val textView: TextView = binding.textSlideshow

        createAnimalViewModel =
            ViewModelProvider(this).get(CreateAnimalViewModel::class.java)
        createAnimalViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { render(status) }
        })
        createAnimalViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it
        }
        Timber.i("PRESSED")
        print("Pressed")
        binding.imageSelect.setOnClickListener {
            showImagePicker(intentLauncher)
        }
        setButtonListener(binding)

        //onTextChange(binding)


        return root
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {

                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context, "THIS", Toast.LENGTH_LONG).show()
        }
    }


    fun setButtonListener(layout: FragmentCreateanimalBinding) {
        registerImagePickerCallback()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        Timber.i("PRESSED")

        layout.confirmCreate.setOnClickListener {
            animal.animalSpecies = layout.speciesField.text.toString()
            animal.animalName = layout.nameField.text.toString()
            animal.region = layout.regionField.text.toString()
            animal.diet = layout.dietField.text.toString()
            animal.email = loggedInViewModel.liveFirebaseUser.value!!.email.toString()
            animal.longitude = mapsViewModel.currentLocation.value!!.longitude
            animal.latitude = mapsViewModel.currentLocation.value!!.latitude
            createAnimalViewModel.addAnimal(loggedInViewModel.liveFirebaseUser,animal)
            FireBaseImageManager.uploadObjectImageToFirebase(loggedInViewModel.liveFirebaseUser.value!!.uid,animal.uid,binding.imagePrev.drawable.toBitmap(),animal,true)


            FirebaseDBManager.update(loggedInViewModel.liveFirebaseUser.value!!.uid, animal.uid,animal)
            //val action = CreateAnimalFragmentDirections.actionNavSlideshowToNavGallery()
            //findNavController().navigate(action)
        }



    }



    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            //i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            requireContext().contentResolver.takePersistableUriPermission(
                                image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                            //animal.image = image.toString()


                            Picasso.get()
                                .load(image)
                                .into(binding.imagePrev)

                            //binding.chooseImage.setText("")
                        } // end of if
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}