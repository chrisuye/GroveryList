package com.seyoum.christian.grocerylist.groceryList

import android.content.Context
import com.seyoum.christian.grocerylist.groceryList.data.GroceryListDatabase
import com.seyoum.christian.grocerylist.groceryList.data.GroceryListEntity
import com.seyoum.christian.grocerylist.groceryList.interfaces.IGroceryListRepo

class GroceryListRepo (context: Context): IGroceryListRepo {
    private val db: GroceryListDatabase = GroceryListDatabase(context)

    override suspend fun getGroceryList(userName: String): List<GroceryListEntity> {
        return db.groceryListDao().getAll(userName)
    }

    override suspend fun addGroceryList(groceryListEntity: GroceryListEntity) {
        db.groceryListDao().insertAll(groceryListEntity)
    }

    override suspend fun updateList(groceryListEntity: GroceryListEntity) {
        db.groceryListDao().updateList(groceryListEntity)
    }

    override suspend fun deleteGroceryList(groceryListEntity: GroceryListEntity) {
        db.groceryListDao().delete(groceryListEntity)
    }

}