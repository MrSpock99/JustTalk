package itis.ru.justtalk.ui.words

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import itis.ru.justtalk.R
import kotlinx.android.synthetic.main.activity_create_group.*

const val GALLERY_REQUEST_CODE = 12
const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 15

class ActivityCreateGroup : AppCompatActivity() {
    var resultIntent: Intent = Intent()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        }
        btn_create_group.setOnClickListener {
            resultIntent.putExtra(ARG_GROUP_NAME, et_group_name.text.toString())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        iv_group_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    val selectedImage = data!!.data.toString()
                    resultIntent.putExtra(ARG_IMAGE_URL, selectedImage)
                    Glide.with(this)
                        .load(selectedImage)
                        .placeholder(R.drawable.image_placeholder)
                        .into(iv_group_image)
                }

            }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            /*READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.getUsersNearby()
                } else {
                    showSnackbar(getString(R.string.snackbar_permission_message))
                }
            }*/
        }
    }
}
