package com.seyoum.christian.grocerylist.groceryList.data

import androidx.room.*

@Dao
interface GroceryListDao {
    @Query("SELECT * FROM grocerylistentity WHERE userName LIKE :userName")
    fun getAll(userName: String): List<GroceryListEntity>

    @Insert
    suspend fun insertAll(vararg groceryListEntity: GroceryListEntity)

    @Delete
    fun delete(groceryListEntity: GroceryListEntity)

    @Update
    fun updateList(vararg groceryListEntity: GroceryListEntity)
}