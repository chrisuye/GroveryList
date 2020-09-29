package com.seyoum.christian.grocerylist.groceryList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.groceryList.data.GroceryListEntity
import com.seyoum.christian.grocerylist.groceryList.interfaces.IGroceryListControl
import com.seyoum.christian.grocerylist.ingredientList.IngredientListActivity
import kotlinx.android.synthetic.main.activity_grocery_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class GroceryListActivity : AppCompatActivity(), IGroceryListControl {
    private lateinit var groceryListRepo: GroceryListRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery_list)
        GlobalScope.launch {
            val count = getGroceryList("").size
            this@GroceryListActivity.runOnUiThread {
                groceryRecyclerView.layoutManager = LinearLayoutManager(this@GroceryListActivity)
                groceryRecyclerView.adapter = GroceryListAdapter(this@GroceryListActivity, count)
            }
        }

        addGroceryListBtn.setOnClickListener {
            val intent = Intent(this, IngredientListActivity::class.java)
            startActivityForResult(intent, ADD_LIST_REQUEST_CODE)
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

    override fun showLoading(loading: Boolean) {
        if (loading)
            groceryListProgress.visibility = View.VISIBLE
        else
            groceryListProgress.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode){
            Activity.RESULT_OK -> {
                when (requestCode) {
                    ADD_LIST_REQUEST_CODE -> {
                        showLoading(true)
                        var loading = true
                        val ingredientListString =
                            data?.getStringExtra(IngredientListActivity.ADD_LIST_EXTRA_KEY)
                        val title = data?.getStringExtra(IngredientListActivity.ADD_TITLE_EXTRA_KEY)
                        if (title != null && ingredientListString != null) {
                            GlobalScope.launch {
                                addGroceryList(GroceryListEntity(title, ingredientListString, ""))
                                loading = false
                            }
                        }

                        while (loading) {
                            showLoading(true)
                        }
                        showLoading(false)
                        GlobalScope.launch {
                            val count = getGroceryList("").size
                            this@GroceryListActivity.runOnUiThread {
                                groceryRecyclerView.layoutManager = LinearLayoutManager(this@GroceryListActivity)
                                groceryRecyclerView.adapter = GroceryListAdapter(this@GroceryListActivity, count)
                            }
                        }
//                        val listType: Type = object : TypeToken<ArrayList<String>>() {}.type
//                        val ingredientList: ArrayList<String> = Gson().fromJson(ingredientListString, listType)
//                        val title = data?.getStringExtra(IngredientListActivity.ADD_TITLE_EXTRA_KEY)
//                        for (ingredient in ingredientList) {
//                            val groceryList = Gson().fromJson(
//                                ingredient,
//                                NutritionList::class.java
//                            )
//                            println(groceryList)
//                        }
//                        println(title)
//                        println(ingredientList)

//                            todostemp.addTodo(todo)
//                            recycle_fram.adapter?.notifyItemInserted(todostemp.getCount())

                    }
                }

            }
        }

    }

    companion object{
        const val ADD_LIST_REQUEST_CODE = 1
    }
}