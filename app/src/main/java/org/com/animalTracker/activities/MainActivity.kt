package org.com.animalTracker.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
import org.com.animalTracker.helpers.checkLocationPermissions
import org.com.animalTracker.helpers.isPermissionGranted
import org.com.animalTracker.models.FireBaseImageManager
//import org.com.animalTracker.models.AnimalJSONStore
import org.com.animalTracker.ui.auth.LoggedInViewModel
import org.com.animalTracker.ui.maps.MapsViewModel
import org.com.animalTracker.utils.json.customTransformation
import org.com.animalTracker.utils.json.readImageUri
import org.com.animalTracker.utils.json.showImagePicker
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHeaderBinding : NavHeaderNavBinding
    private lateinit var loggedInViewModel : LoggedInViewModel
    private lateinit var headerView : View
    private lateinit var intentLauncher : ActivityResultLauncher<Intent>
    private val mapsViewModel : MapsViewModel by viewModels()
    var nightModeOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        Timber.i("Animal Tracker Application Started")
        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        initNavHeader()
        if(checkLocationPermissions(this)) {
            mapsViewModel.updateCurrentLocation()
        }
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.mapsFragment), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        val navView = homeBinding.navView
        navView.setupWithNavController(navController)

    }

    public override fun onStart()
    {
        super.onStart()
        registerImagePickerCallback()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
                updateNavHeader(firebaseUser)
        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })

        navHeaderBinding.imageView.setOnClickListener {
            showImagePicker(intentLauncher)
        }

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
                    Timber.i("Loading Existing Default imageUri")
                    FireBaseImageManager.updateDefaultImage(
                        currentUser.uid,
                        R.drawable.ic_launcher_homer,
                        navHeaderBinding.imageView
                    )
                }
            } else // load existing image from firebase
            {
                Timber.i("Loading Existing imageUri")
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

    fun swapTheme(item: MenuItem)
    {
        if(nightModeOn ==false)
        {
            nightModeOn =true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else
        {
            nightModeOn =false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        ; //For night mode theme
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("DX registerPickerCallback() ${readImageUri(result.resultCode, result.data).toString()}")
                            FireBaseImageManager
                                .updateUserImage(loggedInViewModel.liveFirebaseUser.value!!.uid,
                                    readImageUri(result.resultCode, result.data),
                                    navHeaderBinding.imageView,
                                    true)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isPermissionGranted(requestCode, grantResults))
            mapsViewModel.updateCurrentLocation()
        else {
            // permissions denied, so use a default location
            mapsViewModel.currentLocation.value = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        }
        Timber.i("LOC : %s", mapsViewModel.currentLocation.value)
    }

}