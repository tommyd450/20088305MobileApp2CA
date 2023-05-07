package org.com.animalTracker.ui.maps

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.com.animalTracker.R
import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.ui.AnimalList.AnimalListViewModel
import org.com.animalTracker.ui.auth.LoggedInViewModel

class MapsFragment : Fragment() {

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val animalListViewModel: AnimalListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    lateinit var loader : AlertDialog

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap
        mapsViewModel.map.isMyLocationEnabled = true
        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )

            mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14f))
            mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true

            animalListViewModel.observableAnimalList.observe(
                viewLifecycleOwner,
                Observer { animals ->
                    animals?.let {
                        render(animals as ArrayList<AnimalModel>)
                        //hideLoader(loader)
                    }
                })
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_maps, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
    private fun render(animalsList: ArrayList<AnimalModel>) {
        var markerColour: Float
        if (animalsList.isNotEmpty()) {
            mapsViewModel.map.clear()
            animalsList.forEach {
                markerColour =
                    if (it.email.equals(this.animalListViewModel.liveFirebaseUser.value!!.email))
                        BitmapDescriptorFactory.HUE_AZURE + 5
                    else
                        BitmapDescriptorFactory.HUE_RED

                mapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        .title("${it.animalName} €${it.email}")
                        .snippet(it.animalSpecies)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColour))
                )
            }
        }
    }
        override fun onResume() {
            super.onResume()

            loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner) { firebaseUser ->
                if (firebaseUser != null) {
                    animalListViewModel.liveFirebaseUser.value = firebaseUser
                    animalListViewModel.load()
                }
            }
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        val item = menu.findItem(R.id.toggleListAnimals) as MenuItem
        item.setActionView(R.layout.toggle_layout)
        val toggleDonations: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
        toggleDonations.isChecked = false

        toggleDonations.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) animalListViewModel.loadAll()
            else animalListViewModel.load()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

}