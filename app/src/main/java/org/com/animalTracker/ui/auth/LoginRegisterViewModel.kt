package org.com.animalTracker.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.auth.FirebaseUser
import org.com.animalTracker.activities.MainActivity
import org.com.animalTracker.databinding.HomeBinding
import org.com.animalTracker.databinding.NavHeaderNavBinding
import org.com.animalTracker.firebase.FirebaseAuthManager

class LoginRegisterViewModel(application: Application) : AndroidViewModel(application) {
    var firebaseAuthManager : FirebaseAuthManager = FirebaseAuthManager(application)
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser


    fun login(email: String?, password: String?) {
        firebaseAuthManager.login(email, password)
    }

    fun register(email: String?, password: String?) {
        firebaseAuthManager.register(email, password)
    }

    fun logout()
    {
        firebaseAuthManager.logOut()
    }


}