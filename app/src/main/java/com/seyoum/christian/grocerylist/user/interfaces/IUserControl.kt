package com.seyoum.christian.grocerylist.user.interfaces

import com.seyoum.christian.grocerylist.user.data.UserEntity

interface IUserControl {
    suspend fun getUser(userName:String, password: String): Boolean
    suspend fun addUser(userEntity: UserEntity)
    suspend fun checkUser(userName: String): Boolean
}