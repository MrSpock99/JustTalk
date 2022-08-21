package itis.ru.justtalk.ui.words.words

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.bumptech.glide.Glide
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.base.BaseActivity
import itis.ru.justtalk.ui.words.PhotoChooseDialogFragment
import itis.ru.justtalk.ui.words.groups.ARG_IMAGE_URL
import itis.ru.justtalk.ui.words.groups.GALLERY_REQUEST_CODE
import itis.ru.justtalk.utils.PhotoChooseDialogCallback
import kotlinx.android.synthetic.main.activity_add_word.*

class AddWordActivity : BaseActivity(), PhotoChooseDialogCallback {
    private lateinit var viewModel: AddWordViewModel
    private var resultIntent: Intent? = null

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

    override fun onPhotoSelected(url: String) {
        resultIntent?.putExtra(ARG_IMAGE_URL, url)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun init() {
        injectDependencies()

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(AddWordViewModel::class.java)
        viewModel.getArguments(intent)
        observePhotoChoose()
        observeAddWordLiveData()
        observeEditWordLiveData()

        btn_add_word.setOnClickListener {
            viewModel.addWordFinish(
                et_word.text.toString(), et_translation.text.toString(),
                switch_choose_photo_mode.isChecked
            )
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
                if (resultIntent == null){
                    resultIntent = response.data
                    setResult(Activity.RESULT_OK, resultIntent)
                }
                if (response.data.getBooleanExtra(ARG_AUTO_PHOTO, false)) {
                    val dialog = PhotoChooseDialogFragment.newInstance(
                        response.data.getStringExtra(
                            ARG_WORD
                        )
                    )
                    dialog.show(supportFragmentManager, null)
                } else {
                    finish()
                }

            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    private fun observeEditWordLiveData() =
        viewModel.editWordLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                et_word.setText(response.data.word)
                et_translation.setText(response.data.translation)
                et_word.setSelection(response.data.word.length)
                et_translation.setSelection(response.data.translation.length)
                btn_add_word.text = getString(R.string.all_edit)
                Glide.with(this)
                    .load(response.data.imageUrl)
                    .into(iv_word_image)
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
