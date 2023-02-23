package org.com.animalTracker.ui.AnimalList

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.core.view.get
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
import org.com.animalTracker.models.AnimalStorage
import timber.log.Timber


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
                animals ->
            animals?.let { render(animals) }
        })

        /*val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = ReportFragmentDirections.actionReportFragmentToDonateFragment()
            findNavController().navigate(action)
        }*/
        val searchView: SearchView = fragBinding.animalSearch
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                Timber.i("SearchAttempt")
                filter(newText)
                return false
            }
        })


            return root
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<AnimalModel> = ArrayList()

        // running a for loop to compare elements.
        for (item in AnimalStorage.animals) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.animalName.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
                Timber.i("Found Thing")
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Timber.i("Search busted")
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            var adp: AnimalAdapter = AnimalAdapter(AnimalStorage.findAll(),this)
            adp.filterList(filteredlist)
            render(filteredlist)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //inflater.inflate(R.menu.menu_report, menu)
        //super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun render(animalList: List<AnimalModel>) {
        fragBinding.recyclerView.adapter = AnimalAdapter(animalList,this)
        if (animalList.isEmpty()) {
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