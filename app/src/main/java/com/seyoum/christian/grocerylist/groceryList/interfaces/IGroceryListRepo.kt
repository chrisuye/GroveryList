package com.seyoum.christian.grocerylist.groceryList.interfaces

import com.seyoum.christian.grocerylist.groceryList.data.GroceryListEntity

interface IGroceryListRepo {
    suspend fun getGroceryList(userName:String): List<GroceryListEntity>
    suspend fun addGroceryList(groceryListEntity: GroceryListEntity)
    suspend fun updateList(groceryListEntity: GroceryListEntity)
    suspend fun deleteGroceryList(groceryListEntity: GroceryListEntity)
}