package itis.ru.justtalk.ui.people

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import itis.ru.justtalk.R
import itis.ru.justtalk.adapters.people.CardItemString
import itis.ru.justtalk.adapters.people.CardPagerAdapterS
import itis.ru.justtalk.repository.UserRepositoryImpl
import itis.ru.justtalk.ui.MainActivity
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import itis.ru.justtalk.utils.ShadowTransformer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_set_age.view.*
import kotlinx.android.synthetic.main.fragment_people.*

class PeopleFragment : Fragment() {
    private var mCardAdapter: CardPagerAdapterS? = null
    private var mCardShadowTransformer: ShadowTransformer? = null

    internal var titlesText = arrayOf(
        " Time Table 0",
        " Time Table 1",
        " Time Table 2",
        " Time Table 3",
        " Time Table 4",
        " Time Table 5",
        " Time Table 6",
        " Time Table 7",
        " Time Table 8",
        " Time Table 9"
    )
    internal var detailsArray = arrayOf(
        "Time table details radom, Lorem ipsum characters ment for testing of programs and characters or displaying random informations",
        "Time table details radom, Lorem ipsum characters ment for testing of programs and characters or displaying random informations",
        "Time table details radom, Lorem ipsum characters ment for testing of programs and characters or displaying random informations",
        "Time table details radom, Lorem ipsum characters ment for testing of programs and characters or displaying random informations",
        "Time table details radom, Lorem ipsum characters ment for testing of programs and characters or displaying random informations",
        "Time table details radom, Lorem ipsum characters ment for testing of programs and characters or displaying random informations",
        "Time table details radom, Lorem ipsum characters ment for testing of programs and characters or displaying random informations",
        "Time table details radom, Lorem ipsum characters ment for testing of programs and characters or displaying random informations",
        "Time table details radom, Lorem ipsum characters ment for testing of programs and characters or displaying random informations",
        "Time table details radom, Lorem ipsum characters ment for testing of programs and characters or displaying random informations"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //showAgeDialog()
        init()
        setToolbarAndBottomNavVisibility()
    }

    private fun init(){
        mCardAdapter = CardPagerAdapterS()
        for (i in titlesText.indices) {
            mCardAdapter!!.addCardItemS(CardItemString(titlesText[i], detailsArray[i]))
        }
        mCardShadowTransformer = ShadowTransformer(viewPager, mCardAdapter)

        viewPager.setAdapter(mCardAdapter)
        viewPager.setPageTransformer(false, mCardShadowTransformer)
        viewPager.setOffscreenPageLimit(3)
    }

    private fun setToolbarAndBottomNavVisibility(){
        (activity as MainActivity).toolbar.visibility = View.VISIBLE
        (activity as MainActivity).bottom_navigation.visibility = View.VISIBLE
    }

   /* private fun showAgeDialog() {
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_set_age, null)
        val builder = context?.let {
            AlertDialog.Builder(it)
                    .setTitle("A little more information")
                    .setView(dialogLayout)
                    .setPositiveButton("OK") { _, i ->
                        UserRepositoryImpl(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
                                .addUserToDb(dialogLayout.et_age.text.toString().toInt(), dialogLayout.spinner_gender.selectedItem.toString(), HashMap<String, Double>())
                    }
        }
        builder?.show()

        btn_go.setOnClickListener {
            (activity as MainActivity).navigateTo(MyProfileFragment(), null)
        }
    }*/

    companion object {
        fun newInstance() = PeopleFragment()
    }
}