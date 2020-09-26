package com.seyoum.christian.grocerylist.user

import android.content.Context
import com.seyoum.christian.grocerylist.user.data.UserDatabase
import com.seyoum.christian.grocerylist.user.data.UserEntity
import com.seyoum.christian.grocerylist.user.interfaces.IUserRepo
import java.lang.Exception

class UserRepo (context: Context): IUserRepo {
    private val db: UserDatabase = UserDatabase(context)

    override suspend fun getUser(userName: String, password: String): Boolean {
        val data = db.userDao().findByUserName(userName)
        try {
            if (data.password == password)
                return true
            return false
        } catch (e:Exception) {
            return false
        }

    }

    override suspend fun checkUser(userName: String): Boolean {
        val data = db.userDao().findByUserName(userName)
        try {
            if (data.userName == userName)
                return false
            return true
        } catch (e:Exception) {
            return true
        }
    }

    override suspend fun addUser(userEntity: UserEntity) {
        db.userDao().insertAll(userEntity)
    }

}