package itis.ru.justtalk.ui.people

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.adapters.people.CardPagerAdapterS
import itis.ru.justtalk.ui.MainActivity
import itis.ru.justtalk.utils.ShadowTransformer
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_people.*
import javax.inject.Inject

class PeopleFragment : Fragment() {
    @Inject
    lateinit var mCardAdapter: CardPagerAdapterS
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: PeopleViewModel
    private lateinit var rootActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        injectDependencies()
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun injectDependencies(){
        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    private fun init(){
        rootActivity = activity as MainActivity
        setToolbarAndBottomNavVisibility()

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(PeopleViewModel::class.java)

        viewModel.getUsers(5)
        observeUsersLiveData()
        observeShowLoadingLiveData()
    }

    private fun setToolbarAndBottomNavVisibility(){
        rootActivity.toolbar.visibility = View.VISIBLE
        rootActivity.bottom_navigation.visibility = View.VISIBLE
    }

    private fun observeUsersLiveData() = viewModel.usersLiveData.observe(this, Observer { list ->
        list?.let {
            for (i in list.indices) {
                mCardAdapter.addCardItemS(list[i])
            }
            val mCardShadowTransformer = ShadowTransformer(viewPager, mCardAdapter)

            viewPager.adapter = mCardAdapter
            viewPager.setPageTransformer(false, mCardShadowTransformer)
            viewPager.offscreenPageLimit = 3
        }
    })

    private fun observeShowLoadingLiveData() =
        viewModel.showLoadingLiveData.observe(this, Observer {
            it?.let { show ->
                rootActivity.showLoading(show)
            }
        })

    companion object {
        fun newInstance() = PeopleFragment()
    }
}
