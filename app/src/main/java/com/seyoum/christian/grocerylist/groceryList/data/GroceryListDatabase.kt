package com.seyoum.christian.grocerylist.groceryList.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GroceryListEntity::class], version = 3)
abstract class GroceryListDatabase : RoomDatabase() {
    abstract fun groceryListDao(): GroceryListDao

    companion object {
        @Volatile
        private var instance: GroceryListDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context): GroceryListDatabase {
            return Room.databaseBuilder(
                context,
                GroceryListDatabase::class.java, "grocery-list.db").fallbackToDestructiveMigration()
                .build()
        }
    }
}