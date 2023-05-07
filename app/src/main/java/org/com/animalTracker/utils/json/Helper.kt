package org.com.animalTracker.utils.json

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View

import androidx.activity.result.ActivityResultLauncher
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Transformation
import org.com.animalTracker.R
import java.io.IOException


fun customTransformation() : Transformation = RoundedTransformationBuilder().borderColor(Color.WHITE).borderWidthDp(2F).cornerRadiusDp(35F).oval(false).build()

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {


    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_profile_image.toString())
    intentLauncher.launch(chooseFile)
}

fun readImageUri(resultCode: Int, data: Intent?): Uri? {
    var uri: Uri? = null
    if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
        try {
            uri = data.data
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return uri
}