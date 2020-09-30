package com.seyoum.christian.grocerylist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.seyoum.christian.grocerylist.groceryList.GroceryListActivity
import com.seyoum.christian.grocerylist.user.CreateAccountFragment
import com.seyoum.christian.grocerylist.user.SignInFragment
import com.seyoum.christian.grocerylist.user.UserRepo
import com.seyoum.christian.grocerylist.user.data.UserDatabase
import com.seyoum.christian.grocerylist.user.data.UserEntity
import com.seyoum.christian.grocerylist.user.interfaces.IUserControl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), IUserControl {
    lateinit var db:UserDatabase
    lateinit var userRepo: UserRepo
    lateinit var signInFragment: SignInFragment
    lateinit var createAccountFragment: CreateAccountFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref = getSharedPreferences(GroceryListActivity.USER, MODE_MULTI_PROCESS)
        val userName = sharedPref.getString(GroceryListActivity.USERNAME, "X!X")

        if (userName != "X!X") {
            val intent = Intent(this, GroceryListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        signInFragment = SignInFragment(this)
        createAccountFragment = CreateAccountFragment(this)
        val fragmentManager = supportFragmentManager

        db = UserDatabase(this)
        userRepo = UserRepo(this)

        GlobalScope.launch {
            val data = db.userDao().getAll()
            data.forEach {
                println(it)
            }
        }

        fragmentManager.beginTransaction().add(R.id.mainLayout, signInFragment, TAG).commit()
    }

    override suspend fun getUser(userName: String, password: String): Boolean {
        userRepo = UserRepo(this)
        return userRepo.getUser(userName, password)
    }

    override suspend fun addUser(userEntity: UserEntity) {
        userRepo = UserRepo(this)
        userRepo.addUser(userEntity)
    }

    override suspend fun checkUser(userName: String): Boolean {
        userRepo = UserRepo(this)
        return userRepo.checkUser(userName)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(TAG)
        if (fragment != null && fragment.isVisible) {
            super.onBackPressed()
        } else {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.mainLayout, signInFragment, TAG)
            fragmentTransaction.commit()
        }
    }

    companion object {
        const val TAG = "SIGN_IN"
    }
}