package org.com.animalTracker.ui.createAnimal

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.com.animalTracker.databinding.FragmentCreateanimalBinding
import org.com.animalTracker.models.AnimalModel
import timber.log.Timber
import com.android.volley.VolleyError;
import org.com.animalTracker.activities.MainActivity
import org.com.animalTracker.main.App


class CreateAnimalFragment : Fragment() {
    lateinit var app : App
    private var _binding: FragmentCreateanimalBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var createAnimalViewModel: CreateAnimalViewModel
    private var mRequestQueue: RequestQueue? = null
    private var mStringRequest: StringRequest? = null
    private val url = "https://api.api-ninjas.com/v1/animals?name="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRequestQueue = Volley.newRequestQueue(activity)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


        _binding = FragmentCreateanimalBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //val textView: TextView = binding.textSlideshow

        createAnimalViewModel =
            ViewModelProvider(this).get(CreateAnimalViewModel::class.java)
        createAnimalViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })
        createAnimalViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it
        }
        Timber.i("PRESSED")
        print("Pressed")
        setButtonListener(binding)

        //onTextChange(binding)



        return root
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,"THIS",Toast.LENGTH_LONG).show()
        }
    }

    fun setButtonListener(layout: FragmentCreateanimalBinding)
    {
        Timber.i("PRESSED")
        layout.confirmCreate.setOnClickListener{

            createAnimalViewModel.addAnimal(AnimalModel(animalSpecies = layout.speciesField.text.toString(),
                animalName = layout.nameField.text.toString(), region = layout.regionField.text.toString(),
                diet = layout.dietField.text.toString() ))
        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}