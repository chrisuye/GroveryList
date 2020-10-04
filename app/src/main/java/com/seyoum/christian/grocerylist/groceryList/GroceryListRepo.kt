package com.seyoum.christian.grocerylist.groceryList

import android.content.Context
import com.seyoum.christian.grocerylist.groceryList.data.GroceryListDatabase
import com.seyoum.christian.grocerylist.groceryList.data.GroceryListEntity
import com.seyoum.christian.grocerylist.groceryList.interfaces.IGroceryListRepo

class GroceryListRepo (context: Context, val userName: String): IGroceryListRepo {
    private val db: GroceryListDatabase = GroceryListDatabase(context)
    private val fullGroceryList: MutableList<GroceryListEntity> = mutableListOf()
    private val groceryList: MutableList<GroceryListEntity> = mutableListOf()

    override suspend fun getGroceryList(userName: String): List<GroceryListEntity> {
        fullGroceryList.addAll(db.groceryListDao().getAll(userName))
        groceryList.addAll(db.groceryListDao().getAll(userName))
        return groceryList
    }

    override suspend fun addGroceryList(groceryListEntity: GroceryListEntity) {
        db.groceryListDao().insertAll(groceryListEntity)
        fullGroceryList.clear()
        groceryList.clear()
        fullGroceryList.addAll(db.groceryListDao().getAll(userName))
        groceryList.addAll(db.groceryListDao().getAll(userName))
    }

    override suspend fun updateList(groceryListEntity: GroceryListEntity) {
        db.groceryListDao().updateList(groceryListEntity)
        fullGroceryList.clear()
        groceryList.clear()
        fullGroceryList.addAll(db.groceryListDao().getAll(userName))
        groceryList.addAll(db.groceryListDao().getAll(userName))
    }

    override suspend fun deleteGroceryList(groceryListEntity: GroceryListEntity) {
        db.groceryListDao().delete(groceryListEntity)
        fullGroceryList.clear()
        groceryList.clear()
        fullGroceryList.addAll(db.groceryListDao().getAll(userName))
        groceryList.addAll(db.groceryListDao().getAll(userName))
    }

    override fun search(newText: String) {
        groceryList.clear()
        for (grocery in fullGroceryList) {
            if (grocery.title.toLowerCase().contains(newText.toLowerCase())) {
                groceryList.add(grocery)
            }
        }
    }

}