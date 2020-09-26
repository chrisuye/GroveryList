package com.seyoum.christian.grocerylist.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.seyoum.christian.grocerylist.GroceryListActivity
import com.seyoum.christian.grocerylist.MainActivity
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.user.data.UserEntity
import com.seyoum.christian.grocerylist.user.interfaces.IUserControl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var createAccount:Button
private lateinit var userName: EditText
private lateinit var firstName: EditText
private lateinit var lastName: EditText
private lateinit var email: EditText
private lateinit var password: EditText

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentFirstName.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateAccountFragment(val userControl: IUserControl) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_create_account, container, false)
        createAccount = view.findViewById(R.id.createAccountBtn)

        userName = view.findViewById(R.id.userName)as EditText
        firstName = view.findViewById(R.id.firstName)as EditText
        lastName = view.findViewById(R.id.lastName)as EditText
        email = view.findViewById(R.id.email)as EditText
        password = view.findViewById(R.id.password)as EditText

        createAccount.setOnClickListener {
            val userEntity = UserEntity(
                userName.text.toString(),
                firstName.text.toString(),
                lastName.text.toString(),
                email.text.toString(),
                password.text.toString())
            GlobalScope.launch {
                if (userControl.checkUser(userName.text.toString())) {
                    userControl.addUser(userEntity)
                    val intent = Intent(activity, GroceryListActivity::class.java)
                    startActivity(intent)
                } else {
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "username already exists", Toast.LENGTH_LONG).show()
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
         * @return A new instance of fragment FragmentFirstName.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateAccountFragment(MainActivity()).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}