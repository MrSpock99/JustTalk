package itis.ru.justtalk.ui.words.test

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mindorks.placeholderview.SwipeDecor
import com.mindorks.placeholderview.SwipePlaceHolderView
import com.mindorks.placeholderview.SwipeViewBuilder
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.utils.ViewModelFactory
import itis.ru.justtalk.utils.dpToPx
import itis.ru.justtalk.utils.getDisplaySize
import kotlinx.android.synthetic.main.fragment_test.*
import javax.inject.Inject

const val ARG_LIST_SIZE: String = "LIST_SIZE"
const val ARG_CORRECT_COUNT: String = "CORRECT_COUNT"

class TestFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: TestViewModel
    private var listSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(TestViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        viewModel.startTest(arguments)
        observeWordListLiveData()
        observeTestGoingOnLiveData()
        observeEndTestLiveData()
    }

    private fun injectDependencies() {
        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    private fun init() {
        setToolbarAndBottomNavVisibility(View.VISIBLE, View.GONE)
        setToolbarTitle(getString(R.string.nav_test_title))

        val bottomMargin = dpToPx(160)
        val windowSize = getDisplaySize(rootActivity.windowManager)
        swipeView.getBuilder<SwipePlaceHolderView, SwipeViewBuilder<SwipePlaceHolderView>>()
            .setDisplayViewCount(1)
            .setSwipeDecor(
                SwipeDecor()
                    .setViewWidth(windowSize.x)
                    .setViewHeight(windowSize.y - bottomMargin)
                    .setViewGravity(Gravity.TOP)
                    .setPaddingTop(20)
                    .setRelativeScale(0.01f)
                    .setSwipeInMsgLayoutId(R.layout.test_swipe_in_msg_view)
                    .setSwipeOutMsgLayoutId(R.layout.test_swipe_out_msg_view)
            )

        btn_hint.setOnClickListener {
            viewModel.getHint()
        }
    }

    private fun observeWordListLiveData() =
        viewModel.wordListLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                response.data.forEach {
                    view?.let { context ->
                        val card = TestCard(context, it, viewModel, swipeView)
                        swipeView.addView(card)
                        viewModel.register(card)
                    }

                }
                listSize = response.data.size
                tv_test_result.text = getString(R.string.test_res_string, 0, listSize)
                progressBar.progress = 0
                progressBar.max = response.data.size
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    private fun observeTestGoingOnLiveData() =
        viewModel.testGoingOnLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                tv_test_result.text = getString(R.string.test_res_string, response.data, listSize)
                progressBar.progress = response.data
            }
        })

    private fun observeEndTestLiveData() =
        viewModel.endTestListLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                response.data.getContentIfNotHandled()?.let {
                    val bundle = Bundle()
                    bundle.putInt(ARG_LIST_SIZE, it.count)
                    bundle.putInt(ARG_CORRECT_COUNT, it.correctCount)
                    rootActivity.navigateTo(EndTestFragment.toString(), bundle)
                }
            }
        })

    companion object {
        fun newInstance() = TestFragment()
    }
}
