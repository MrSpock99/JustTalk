package itis.ru.justtalk.ui.words.groups

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_create_group.*
import javax.inject.Inject

const val GALLERY_REQUEST_CODE = 12
const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 15

class CreateGroupActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: CreateGroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            viewModel.onPhotoChooseResult(requestCode, data)
        else
            showSnackbar(getString(R.string.snackbar_error_message))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showSnackbar(getString(R.string.snackbar_permission_message))
                }
            }
        }
    }

    private fun init() {
        injectDependencies()
        checkPermissions()

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(CreateGroupViewModel::class.java)
        observePhotoChoose()
        observeCreateGroupLiveData()

        btn_create_group.setOnClickListener {
            viewModel.createGroupFinish(et_group_name.text.toString())
        }
        iv_group_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
    }

    private fun injectDependencies() {
        (application as BaseApplication).appComponent.inject(this)
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        }
    }

    private fun observePhotoChoose() =
        viewModel.photoChooseSuccessLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                Glide.with(this)
                    .load(response.data)
                    .placeholder(R.drawable.image_placeholder)
                    .into(iv_group_image)
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    private fun observeCreateGroupLiveData() =
        viewModel.createGroupFinishLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                setResult(Activity.RESULT_OK, response.data)
                finish()
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    fun showSnackbar(message: String) {
        Snackbar.make(
            scrollView,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
