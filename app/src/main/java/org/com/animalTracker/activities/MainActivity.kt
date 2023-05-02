package org.com.animalTracker.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import org.com.animalTracker.R
import org.com.animalTracker.databinding.HomeBinding
import org.com.animalTracker.databinding.NavHeaderNavBinding
import org.com.animalTracker.models.FireBaseImageManager
//import org.com.animalTracker.models.AnimalJSONStore
import org.com.animalTracker.ui.auth.LoggedInViewModel
import org.com.animalTracker.utils.json.customTransformation
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHeaderBinding : NavHeaderNavBinding
    private lateinit var loggedInViewModel : LoggedInViewModel
    private lateinit var headerView : View



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //AnimalJSONStore.context = applicationContext
        //AnimalJSONStore.init()
        Timber.plant(Timber.DebugTree())
        //animalStorage = AnimalStorage()
        Timber.i("Animal Tracker Application Started")
        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        initNavHeader()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        val navView = homeBinding.navView
        navView.setupWithNavController(navController)

    }

    public override fun onStart()
    {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            //if (firebaseUser != null)
           // {
                updateNavHeader(firebaseUser)
            //}


        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun updateNavHeader(currentUser: FirebaseUser) {
        FireBaseImageManager.imageUri.observe(this) { result ->
            if (result == Uri.EMPTY) {
                Timber.i("DX NO Existing imageUri")
                if (currentUser.photoUrl != null) {
                    //if you're a google user
                    FireBaseImageManager.updateUserImage(
                        currentUser.uid,
                        currentUser.photoUrl,
                        navHeaderBinding.imageView,
                        true
                    )
                } else {
                    Timber.i("DX Loading Existing Default imageUri")
                    FireBaseImageManager.updateDefaultImage(
                        currentUser.uid,
                        R.drawable.ic_launcher_homer,
                        navHeaderBinding.imageView
                    )
                }
            } else // load existing image from firebase
            {
                Timber.i("DX Loading Existing imageUri")
                FireBaseImageManager.updateUserImage(
                    currentUser.uid,
                    FireBaseImageManager.imageUri.value,
                    navHeaderBinding.imageView, false
                )
            }
        }
        navHeaderBinding.email.text = currentUser.email
        if(currentUser.displayName != null)
            navHeaderBinding.title.text = currentUser.displayName
    }

    private fun initNavHeader() {

        headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderNavBinding.bind(headerView)
    }


    fun signOut(item: MenuItem) {
        loggedInViewModel.logOut()
        //Launch org.com.animalTracker.activities.Login activity and clear the back stack to stop navigating back to the Home activity
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


}