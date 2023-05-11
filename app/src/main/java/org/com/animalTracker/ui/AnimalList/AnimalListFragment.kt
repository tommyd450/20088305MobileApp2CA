package org.com.animalTracker.ui.AnimalList

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.com.animalTracker.R
import org.com.animalTracker.adapters.AnimalAdapter
import org.com.animalTracker.adapters.AnimalClickListener
import org.com.animalTracker.databinding.FragmentAnimallistBinding
import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.models.FirebaseDBManager
import org.com.animalTracker.ui.auth.LoggedInViewModel
import org.com.animalTracker.utils.json.SwipeToDeleteCallback
import org.com.animalTracker.utils.json.SwipeToEditCallback
import timber.log.Timber


class AnimalListFragment : Fragment() , AnimalClickListener{

    private var _fragBinding: FragmentAnimallistBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var animalListViewModel: AnimalListViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

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
            animals?.let { render(animals,animalListViewModel.readOnly.value!!) }
        })


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

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerView.adapter as AnimalAdapter
                if(animalListViewModel.readOnly!!.value ==false)
                {

                    animalListViewModel.removeAnimal(adapter.getItemAt(viewHolder.adapterPosition))
                    adapter.removeAt(viewHolder.adapterPosition)
                    animalListViewModel.load()
                }

                //
            }
        }

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerView.adapter as AnimalAdapter
                if(animalListViewModel.readOnly!!.value ==false) {
                    onAnimalClick(adapter.getItemAt(viewHolder.adapterPosition))
                }
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)

        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)
        setSwipeRefresh()
        checkSwipeRefresh()
        return root
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<AnimalModel> = ArrayList()

        // running a for loop to compare elements.
        for (item in animalListViewModel.observableAnimalList.value!!) {
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
            //var adp: AnimalAdapter = AnimalAdapter(AnimalJSONStore.findAll(),this)
            //adp.filterList(filteredlist)
            render(filteredlist,animalListViewModel.readOnly.value!!)
        }
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun render(animalList: ArrayList<AnimalModel>,readOnly:Boolean) {
        fragBinding.recyclerView.adapter = AnimalAdapter(animalList,this, readOnly)
        if (animalList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onAnimalClick(animal: AnimalModel) {
        if(animalListViewModel.readOnly.value == false)
        {
            val action = AnimalListFragmentDirections.actionNavGalleryToAnimalDetails(animal)
            findNavController().navigate(action)
        }

    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        animalListViewModel.load()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                animalListViewModel.liveFirebaseUser.value = firebaseUser
                animalListViewModel.load()
            }
        })
        setSwipeRefresh()
        checkSwipeRefresh()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_list, menu)
        val item = menu.findItem(R.id.toggleListAnimals) as MenuItem
        item.setActionView(R.layout.toggle_layout)
        val toggleAnimals: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
        toggleAnimals.isChecked = false

        toggleAnimals.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) animalListViewModel.loadAll()
            else animalListViewModel.load()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun setSwipeRefresh() {

        fragBinding.swiperefresh.setOnRefreshListener {
            if(!animalListViewModel.readOnly.value!!)
            {

            }
            fragBinding.swiperefresh.isRefreshing = true
            animalListViewModel.load()
            fragBinding.swiperefresh.isRefreshing = false

        }
    }

    fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}