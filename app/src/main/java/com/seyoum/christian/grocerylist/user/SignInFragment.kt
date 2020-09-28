package com.seyoum.christian.grocerylist.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.seyoum.christian.grocerylist.MainActivity
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.groceryList.GroceryListActivity
import com.seyoum.christian.grocerylist.user.interfaces.IUserControl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignIn.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment(val userControl: IUserControl) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var createAccount: Button
    private lateinit var signIn: Button
    private lateinit var userName: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        createAccount = view.findViewById(R.id.createAccount)
        signIn = view.findViewById((R.id.signIn))

        userName = view.findViewById(R.id.userIn) as EditText
        password = view.findViewById(R.id.passwordIn) as EditText

        createAccount.setOnClickListener {
            val firstNameFragment = CreateAccountFragment(MainActivity())
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.mainLayout, firstNameFragment)
            fragmentTransaction?.commit()
        }

        signIn.setOnClickListener {
            userName = view.findViewById(R.id.userIn)
            password = view.findViewById(R.id.passwordIn)
            GlobalScope.launch {
                if (userControl.getUser(userName.text.toString(), password.text.toString())) {
                    val fm: FragmentManager = activity!!.supportFragmentManager
                    for (i in 0 until fm.backStackEntryCount) {
                        fm.popBackStack()
                    }
                    val intent = Intent(activity, GroceryListActivity::class.java)
                    startActivity(intent)
                } else {
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "wrong username or password", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignIn.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment(MainActivity()).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}