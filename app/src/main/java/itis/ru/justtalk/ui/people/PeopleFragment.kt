package itis.ru.justtalk.ui.people

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import itis.ru.justtalk.R
import itis.ru.justtalk.repository.UserRepositoryImpl
import kotlinx.android.synthetic.main.dialog_set_age.view.*

class PeopleFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showAgeDialog() {
        /*val dialogLayout = layoutInflater.inflate(R.layout.dialog_set_age, null)
        val builder = context?.let {
            AlertDialog.Builder(it)
                    .setTitle("A little more information")
                    .setView(dialogLayout)
                    .setPositiveButton("OK") { _, i ->
                        UserRepositoryImpl(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
                                .addUserToDb(dialogLayout.et_age.text.toString().toInt(), dialogLayout.spinner_gender.selectedItem.toString(), HashMap<String, Double>())
                    }
        }
        builder?.show()*/
    }

    companion object {
        fun newInstance() = PeopleFragment()
    }
}