package com.seyoum.christian.grocerylist.groceryList.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroceryListEntity(
    @PrimaryKey(autoGenerate = true) var index: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "ingredient") var ingredient: String,
    @ColumnInfo(name = "userName") var userName: String
)