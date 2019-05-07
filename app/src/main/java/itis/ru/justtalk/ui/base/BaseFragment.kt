package itis.ru.justtalk.ui.base

import android.content.Context
import android.support.v4.app.Fragment
import android.view.MenuItem
import itis.ru.justtalk.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

open class BaseFragment : Fragment() {
    lateinit var rootActivity: MainActivity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        rootActivity = activity as MainActivity
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                rootActivity.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setArrowToolbarVisibility(show: Boolean) {
        rootActivity.supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    fun setToolbarTitle(title: String) {
        rootActivity.toolbar.tv_toolbar_title.text = title
    }

    fun setToolbarAndBottomNavVisibility(toolbarVisibility: Int, bottomNavVisibility: Int) {
        rootActivity.toolbar.visibility = toolbarVisibility
        rootActivity.bottom_navigation.visibility = bottomNavVisibility
    }
}
