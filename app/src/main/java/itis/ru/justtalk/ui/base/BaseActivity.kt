package itis.ru.justtalk.ui.base

import android.support.v7.app.AppCompatActivity
import itis.ru.justtalk.utils.ViewModelFactory
import javax.inject.Inject

const val REQUEST_CODE = "requestCode"

open class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
}
