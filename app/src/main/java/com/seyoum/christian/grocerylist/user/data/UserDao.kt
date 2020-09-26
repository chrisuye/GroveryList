package com.seyoum.christian.grocerylist.user.data

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM userentity")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM userentity WHERE userName LIKE :userName")
    fun findByUserName(userName: String): UserEntity

    @Insert
    fun insertAll(vararg user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Update
    fun updateUser(vararg user: UserEntity)
}