package org.com.animalTracker.ui.createAnimal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import org.com.animalTracker.databinding.FragmentCreateanimalBinding
import org.com.animalTracker.models.AnimalModel
import timber.log.Timber

class CreateAnimalFragment : Fragment() {

    private var _binding: FragmentCreateanimalBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var createAnimalViewModel: CreateAnimalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


        _binding = FragmentCreateanimalBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textSlideshow

        createAnimalViewModel =
            ViewModelProvider(this).get(CreateAnimalViewModel::class.java)
        createAnimalViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })
        createAnimalViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
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
        layout.createButton.setOnClickListener{
            layout.nameField.text
            createAnimalViewModel.addAnimal(AnimalModel(animalSpecies = layout.speciesField.text.toString(), animalName = layout.nameField.text.toString(), region = layout.regionField.text.toString()))
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}