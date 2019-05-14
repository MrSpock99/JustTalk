package itis.ru.justtalk.ui.words.test

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_end_test.*
import javax.inject.Inject

class EndTestFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: EndTestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(EndTestViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_end_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTestResult(arguments)
        observeTestResultLiveData()
    }

    private fun injectDependencies() {
        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    private fun observeTestResultLiveData() =
        viewModel.testResultLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                tv_result.text = getString(
                    R.string.end_test_res_string,
                    response.data.correctCount,
                    response.data.count
                )
            }
        })

    companion object {
        fun newInstance(bundle: Bundle?): EndTestFragment {
            val fragment = EndTestFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
