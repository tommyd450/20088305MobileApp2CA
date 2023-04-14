package org.com.animalTracker.firebase

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.navigation.ui.AppBarConfiguration

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.com.animalTracker.databinding.HomeBinding
import org.com.animalTracker.databinding.NavHeaderNavBinding
import timber.log.Timber


class FirebaseAuthManager(application: Application) {

    private var application: Application? = null

    var firebaseAuth: FirebaseAuth? = null
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var loggedOut = MutableLiveData<Boolean>()
    var errorStatus = MutableLiveData<Boolean>()


    init {
        this.application = application
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth!!.currentUser != null) {
            liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
            loggedOut.postValue(false)
            errorStatus.postValue(false)
        }
    }

    fun login(email: String?, password: String?) {
        firebaseAuth!!.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
                    loggedOut.postValue(false)
                    errorStatus.postValue(false)
                    Timber.i("LOGIN SUCCESS")


                } else {
                    Timber.i(""+email+" "+password+" ")
                    Timber.i("Login Failure: $task.exception!!.message")
                    errorStatus.postValue(true)
                }
            }
    }

    fun register(email: String?, password: String?) {
        firebaseAuth!!.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
                    errorStatus.postValue(false)

                } else {
                    Timber.i("Registration Failure: $task.exception!!.message")
                    errorStatus.postValue(true)
                }
            }
    }

    fun logOut() {
        firebaseAuth!!.signOut()
        loggedOut.postValue(true)
        errorStatus.postValue(false)
    }




}