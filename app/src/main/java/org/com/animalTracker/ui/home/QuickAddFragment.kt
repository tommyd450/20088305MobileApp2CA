package org.com.animalTracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.com.animalTracker.databinding.FragmentHomeBinding
import timber.log.Timber
import org.com.animalTracker.adapters.AnimalAdapter
import org.com.animalTracker.adapters.AnimalClickListener

import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.models.FirebaseDBManager
import org.com.animalTracker.models.TempStore
import org.com.animalTracker.ui.auth.LoggedInViewModel

import org.json.JSONArray
import org.json.JSONException


class QuickAddFragment : Fragment(), AnimalClickListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mRequestQueue: RequestQueue? = null
    private var stringRequest: StringRequest? = null
    lateinit var quickAddViewModel : QuickAddViewModel
    private val url = "https://api.api-ninjas.com/v1/animals?name="
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        quickAddViewModel =
            ViewModelProvider(this).get(QuickAddViewModel::class.java)


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.recyclerView2.layoutManager = LinearLayoutManager(activity)


        quickAddViewModel =
            ViewModelProvider(this).get(QuickAddViewModel::class.java)
        quickAddViewModel.observableAnimalList.observe(viewLifecycleOwner, Observer {
                animals -> animals?.let { render(animals) }
        })


        val searchView: SearchView = binding.animalSearch
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                TempStore.tanimals.clear()
                getData(binding.animalSearch.query.toString())


                return false
            }


            override fun onQueryTextChange(newText: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                Timber.i("SearchAttempt")
                //filter(newText)
                return false
            }
        })
        return root
    }

    private fun getData(name:String) {
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(activity?.applicationContext )

        // String Request initialized


        val stringRequest = object: StringRequest(
            Method.GET, url+name,
            Response.Listener<String> { response ->

                    Timber.i("Response is: " + response.toString())
                    var obj = JSONArray(response)
                    for (i in 0 until obj.length()) {
                        Timber.i(obj.getJSONObject(i).getString("name"))
                        var species :String = ""
                        var name : String = ""
                        var region :  String = ""
                        var diet : String = "Unknown"
                        try {

                            region =  obj.getJSONObject(i).getString("locations")
                            species  = obj.getJSONObject(i).getJSONObject("taxonomy").getString("scientific_name")
                            name  = obj.getJSONObject(i).getString("name")
                            diet = obj.getJSONObject(i).getJSONObject("characteristics").getString("diet")

                        } catch (e: JSONException)
                        {

                        }

                        TempStore.create(AnimalModel(animalName = name,
                            region = region,
                            animalSpecies = species,
                            diet = diet ))
                        render(TempStore.findAll())
                    }

            },
            Response.ErrorListener { Timber.i("Nope didnt Work") })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()


                headers["x-api-key"] =org.com.animalTracker.BuildConfig.APIKEY

                return headers
            }
        }
        mRequestQueue!!.add(stringRequest)

    }

    private fun render(animalList: List<AnimalModel>) {
        binding.recyclerView2.adapter = AnimalAdapter(animalList,this,false)
        if (animalList.isEmpty()) {
            binding.recyclerView2.visibility = View.GONE
        } else {
            binding.recyclerView2.visibility = View.VISIBLE
        }
    }

    override fun onAnimalClick(animal: AnimalModel) {


        Timber.i("Is it null")
        quickAddViewModel.addAnimal(loggedInViewModel.liveFirebaseUser,animal)
        //val action = HomeFragmentDirections.actionNavHomeToNavGallery()
        //findNavController().navigate(action)

    }

    override fun onResume() {
        super.onResume()
        //quickAddViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}