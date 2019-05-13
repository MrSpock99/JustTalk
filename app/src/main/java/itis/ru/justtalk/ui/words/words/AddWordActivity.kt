package itis.ru.justtalk.ui.words.words

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.words.groups.GALLERY_REQUEST_CODE
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_add_word.*
import javax.inject.Inject

class AddWordActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: AddWordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            viewModel.onPhotoChooseResult(requestCode, data)
        else
            showSnackbar(getString(R.string.snackbar_error_message))
    }

    private fun init() {
        injectDependencies()

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(AddWordViewModel::class.java)
        observePhotoChoose()
        observeAddWordLiveData()

        btn_add_word.setOnClickListener {
            viewModel.addWordFinish(et_word.text.toString(), et_translation.text.toString())
        }

        iv_word_image.setOnClickListener {
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

    private fun observePhotoChoose() =
        viewModel.photoChooseSuccessLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                Glide.with(this)
                    .load(response.data)
                    .placeholder(R.drawable.image_placeholder)
                    .into(iv_word_image)
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    private fun observeAddWordLiveData() =
        viewModel.addWordFinishLiveData.observe(this, Observer { response ->
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
