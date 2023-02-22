package org.com.animalTracker.ui.AnimalList

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.com.animalTracker.R
import org.com.animalTracker.adapters.AnimalAdapter
import org.com.animalTracker.adapters.AnimalClickListener
import org.com.animalTracker.databinding.FragmentAnimallistBinding
import org.com.animalTracker.models.AnimalModel


class AnimalListFragment : Fragment() , AnimalClickListener{

    private var _fragBinding: FragmentAnimallistBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var animalListViewModel: AnimalListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentAnimallistBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        animalListViewModel = ViewModelProvider(this).get(AnimalListViewModel::class.java)
        animalListViewModel.observableAnimalList.observe(viewLifecycleOwner, Observer {
                donations ->
            donations?.let { render(donations) }
        })

        /*val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = ReportFragmentDirections.actionReportFragmentToDonateFragment()
            findNavController().navigate(action)
        }*/
        return root
    }

    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_report, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun render(donationsList: List<AnimalModel>) {
        fragBinding.recyclerView.adapter = AnimalAdapter(donationsList,this)
        if (donationsList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            //fragBinding.donationsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            //fragBinding.donationsNotFound.visibility = View.GONE
        }
    }

    override fun onDonationClick(animal: AnimalModel) {
        //val action = ReportFragmentDirections.actionReportFragmentToDonationDetailFragment(donation.id)
        //findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        animalListViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}