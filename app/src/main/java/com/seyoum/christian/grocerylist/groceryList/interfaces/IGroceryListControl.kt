package com.seyoum.christian.grocerylist.groceryList.interfaces

import com.seyoum.christian.grocerylist.groceryList.data.GroceryListEntity

interface IGroceryListControl {
    suspend fun getGroceryList(userName:String): List<GroceryListEntity>
    suspend fun addGroceryList(groceryListEntity: GroceryListEntity)
    fun showLoading(loading: Boolean)
}