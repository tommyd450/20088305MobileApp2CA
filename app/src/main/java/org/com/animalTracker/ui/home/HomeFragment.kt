package org.com.animalTracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.com.animalTracker.databinding.FragmentHomeBinding
import timber.log.Timber
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var mRequestQueue: RequestQueue? = null
    private var stringRequest: StringRequest? = null
    private val url = "https://api.api-ninjas.com/v1/animals?name=dog"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        //homeViewModel.text.observe(viewLifecycleOwner) {
         //   textView.text = it
        //}
        getData()
        return root
    }

    private fun getData() {
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(activity?.applicationContext )

        // String Request initialized


        val stringRequest = object: StringRequest(
            Method.GET, url,
            Response.Listener<String> { response ->
                Timber.i("Response is: " + response.substring(0,500))
               var obj =  JSONArray(response.toString())
                Timber.i("Test : "+obj[2])
            },
            Response.ErrorListener { Timber.i("Nope didnt Work") })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["x-api-key"] = "79qFpzBqdJ2QnmWDAa2hCw==re8i97z6KmQmpBIA"
                return headers
            }

        }
        //var obj = JSONObject(stringRequest)
        mRequestQueue!!.add(stringRequest)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}