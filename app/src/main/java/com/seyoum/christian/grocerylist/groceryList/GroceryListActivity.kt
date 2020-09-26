package com.seyoum.christian.grocerylist.groceryList

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.groceryList.data.GroceryListEntity
import com.seyoum.christian.grocerylist.groceryList.interfaces.IGroceryListControl
import kotlinx.android.synthetic.main.activity_grocery_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GroceryListActivity : AppCompatActivity(), IGroceryListControl {
    private lateinit var groceryListRepo: GroceryListRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery_list)
        GlobalScope.launch {
            var count = getGroceryList("hey").size
            count = 3
            this@GroceryListActivity.runOnUiThread {
                groceryRecyclerView.layoutManager = LinearLayoutManager(this@GroceryListActivity)
                groceryRecyclerView.adapter = GroceryListAdapter(this@GroceryListActivity, count)
            }
        }

    }

    override suspend fun getGroceryList(userName: String): List<GroceryListEntity> {
        groceryListRepo = GroceryListRepo(this)
        return groceryListRepo.getGroceryList(userName)
    }

    override suspend fun addGroceryList(groceryListEntity: GroceryListEntity) {
        groceryListRepo = GroceryListRepo(this)
        groceryListRepo.addGroceryList(groceryListEntity)
    }
}