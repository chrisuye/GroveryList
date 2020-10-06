package com.seyoum.christian.grocerylist.groceryList

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.seyoum.christian.grocerylist.MainActivity
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.groceryList.data.GroceryListEntity
import com.seyoum.christian.grocerylist.groceryList.interfaces.IGroceryListControl
import com.seyoum.christian.grocerylist.ingredientList.IngredientListActivity
import com.seyoum.christian.grocerylist.listDetail.ListDetailActivity
import kotlinx.android.synthetic.main.activity_grocery_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class GroceryListActivity : AppCompatActivity(), IGroceryListControl {
    private lateinit var groceryListRepo: GroceryListRepo
    private var userName: String? = ""
    private lateinit var sharedPref: SharedPreferences
    private lateinit var showingList: GroceryListEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery_list)
        userName = intent.getStringExtra(USER) ?: DEFAULT_USER
        sharedPref = getSharedPreferences(USER, MODE_MULTI_PROCESS)

        if (userName == DEFAULT_USER) {
            userName = sharedPref.getString(USERNAME, "")
        } else {
            with (sharedPref.edit()) {
                putString(USERNAME, userName)
                commit()
            }
        }
        GlobalScope.launch {
            if (userName != null) {
                this@GroceryListActivity.runOnUiThread {
                    groceryRecyclerView.layoutManager = LinearLayoutManager(this@GroceryListActivity)
                    groceryRecyclerView.adapter =
                        GroceryListAdapter(this@GroceryListActivity, userName!!)
                }
            }
        }

        addGroceryListBtn.setOnClickListener {
            val intent = Intent(this, IngredientListActivity::class.java)
            startActivityForResult(intent, ADD_LIST_REQUEST_CODE)
        }

    }

    override suspend fun getGroceryList(userName: String): List<GroceryListEntity> {
        groceryListRepo = GroceryListRepo(this, userName)
        return groceryListRepo.getGroceryList(userName)
    }

    override suspend fun addGroceryList(groceryListEntity: GroceryListEntity) {
        groceryListRepo = userName?.let { GroceryListRepo(this, it) }!!
        groceryListRepo.addGroceryList(groceryListEntity)
    }

    override suspend fun updateList(groceryListEntity: GroceryListEntity) {
        groceryListRepo = userName?.let { GroceryListRepo(this, it) }!!
        groceryListRepo.updateList(groceryListEntity)
    }

    override fun showLoading(loading: Boolean) {
        if (loading)
            groceryListProgress.visibility = View.VISIBLE
        else
            groceryListProgress.visibility = View.GONE
    }

    override fun launchListDetail(ingredients: GroceryListEntity) {
        showingList = ingredients
        val intent = Intent(this, ListDetailActivity::class.java)
        intent.putExtra(LAUNCH_DETAIL_LIST, ingredients.ingredient)
        startActivityForResult(intent, SHOWING)
    }

    override fun search(newText: String) {
        groceryListRepo.search(newText)
    }

    override fun deleteGroceryList(groceryListEntity: GroceryListEntity, position: Int) {
        var deleting = true
        showLoading(deleting)
        GlobalScope.launch {
            groceryListRepo.deleteGroceryList(groceryListEntity)
            deleting = false
        }
        while (deleting){
            showLoading(deleting)
        }
        showLoading(deleting)
        groceryRecyclerView.adapter?.notifyItemRemoved(position)
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
                                userName?.let {
                                    GroceryListEntity(0, title, ingredientListString,
                                        it
                                    )
                                }?.let { addGroceryList(it) }
                                loading = false
                            }
                        }

                        while (loading) {
                            showLoading(true)
                        }
                        showLoading(false)
                        GlobalScope.launch {
                            this@GroceryListActivity.runOnUiThread {
                                groceryRecyclerView.layoutManager = LinearLayoutManager(this@GroceryListActivity)
                                groceryRecyclerView.adapter = userName?.let {
                                    GroceryListAdapter(this@GroceryListActivity,
                                        it
                                    )
                                }
                            }
                        }

                    }

                    SHOWING -> {
                        showLoading(true)
                        var loading = true
                        val nutritionListString = data?.getStringExtra(ListDetailActivity.UPDATE_LIST)
                        if (nutritionListString != null) {
                            showingList.ingredient = nutritionListString
                            GlobalScope.launch {
                                groceryListRepo.updateList(showingList)
                                loading = false
                            }
                        }
                        while (loading) {
                            showLoading(true)
                        }
                        showLoading(false)
                        GlobalScope.launch {
                            this@GroceryListActivity.runOnUiThread {
                                groceryRecyclerView.layoutManager = LinearLayoutManager(this@GroceryListActivity)
                                groceryRecyclerView.adapter = userName?.let {
                                    GroceryListAdapter(this@GroceryListActivity,
                                        it
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.grocery_search_logout_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){

            R.id.logout -> {
                with (sharedPref.edit()) {
                    putString(USERNAME, DEFAULT_USER)
                    commit()
                }
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    companion object{
        const val ADD_LIST_REQUEST_CODE = 1
        const val LAUNCH_DETAIL_LIST = "INGREDIENTS"
        const val USER = "USER"
        const val USERNAME = "USERNAME"
        const val DEFAULT_USER = "X!X"
        const val SHOWING = 2
    }
}