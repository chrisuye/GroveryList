package com.seyoum.christian.grocerylist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import androidx.room.Room
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signInFragment = SignInFragment(this)
        val fragmentManager = supportFragmentManager

        db = UserDatabase(this)
        userRepo = UserRepo(this)

        GlobalScope.launch {
            val data = db.userDao().getAll()
            data.forEach {
                println(it)
            }
        }

        fragmentManager.beginTransaction().add(R.id.mainLayout, signInFragment).commit()
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
}