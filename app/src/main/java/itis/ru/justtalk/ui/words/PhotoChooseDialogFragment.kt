package itis.ru.justtalk.ui.words

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.adapters.PhotoChooseAdapter
import itis.ru.justtalk.utils.PhotoChooseDialogCallback
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.dialog_choose_photo.*
import javax.inject.Inject

const val ARG_KEYWORD = "keyword"

class PhotoChooseDialogFragment : DialogFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: PhotoChooseViewModel
    private var callback: PhotoChooseDialogCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as PhotoChooseDialogCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_choose_photo, container, false)
        injectDependencies()
        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(PhotoChooseViewModel::class.java)
        viewModel.getPhotos(arguments?.getString(ARG_KEYWORD).toString())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePhotos()
    }

    private fun injectDependencies() {
        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    private fun observePhotos() = viewModel.photosLiveData.observe(this, Observer { response ->
        if (response?.data != null) {
            val adapter = PhotoChooseAdapter { url ->
                callback?.onPhotoSelected(url)
            }
            adapter.submitList(response.data)
            rv_photos.layoutManager =
                GridLayoutManager(context, 2)
            rv_photos.adapter = adapter
        }
    })

    companion object {
        fun newInstance(keyword: String): PhotoChooseDialogFragment {
            val fragment = PhotoChooseDialogFragment()
            val bundle = Bundle()
            bundle.putString(ARG_KEYWORD, keyword)
            fragment.arguments = bundle
            return fragment
        }
    }
}