package org.com.animalTracker.ui.auth

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import org.com.animalTracker.R
import org.com.animalTracker.activities.MainActivity

import org.com.animalTracker.databinding.FragmentAuthBinding
import org.com.animalTracker.models.AnimalModel
import org.com.animalTracker.ui.AnimalList.AnimalListViewModel
import timber.log.Timber


class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val fragBinding get() = _binding!!
    private lateinit var authViewModel: LoginRegisterViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("Register Resumed")
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("Frag Resumed")
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        val root = fragBinding.root


        authViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
        authViewModel.firebaseAuthManager.logOut()
        setButtonListener(fragBinding)
        return root
    }


    override fun onResume() {
        authViewModel.firebaseAuthManager.logOut()
        super.onResume()
    }

    override fun onDestroyView() {
        authViewModel.firebaseAuthManager.logOut()
        _binding = null
        super.onDestroyView()

    }

    fun setButtonListener(layout: FragmentAuthBinding)
    {

        /*layout.confirmCreate.setOnClickListener{

            createAnimalViewModel.addAnimal(
                AnimalModel(animalSpecies = layout.speciesField.text.toString(),
                animalName = layout.nameField.text.toString(), region = layout.regionField.text.toString(),
                diet = layout.dietField.text.toString() )
            )
        }*/
        layout.LogInButton.setOnClickListener {
            authViewModel.login(email = layout.editTextTextPersonName.text.toString(), password = layout.editTextTextPassword.text.toString())

            if(authViewModel.firebaseAuthManager.loggedOut.value== false)
            {
                Timber.i("PRESSED")
                authViewModel.liveFirebaseUser.value?.let { it1 -> updateNavInfo(it1) }
                findNavController().navigate(R.id.nav_home)

            }
        }
        layout.createAccountButton.setOnClickListener{
            authViewModel.register(email = layout.editTextTextPersonName.text.toString(), password = layout.editTextTextPassword.text.toString())
        }
    }

    fun updateNavInfo(currentUser: FirebaseUser)
    {

        var act = (activity as MainActivity)
        act.updateNavHeader(currentUser)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

}