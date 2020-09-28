package com.seyoum.christian.grocerylist.ingredientList

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.ingredientList.interfaces.IIngredientListControl
import com.seyoum.christian.grocerylist.ingredientList.network.model.IngredientList
import com.seyoum.christian.grocerylist.ingredientList.network.model.ViewModel
import kotlinx.android.synthetic.main.activity_ingredient_list.*
import okhttp3.*
import java.io.IOException

class IngredientListActivity : AppCompatActivity(), IIngredientListControl {
    private var count = 0
    private var next: String? = ""
    private val ingredientListRepo = IngredientListRepo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient_list)

        ingredientListRecycleView.layoutManager = LinearLayoutManager(this)
        val url = "https://wger.de/api/v2/ingredient/?format=json"
        fetchJson(url)

        ingredientListRecycleView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!ingredientListRecycleView.canScrollVertically(1)) {
                    if (next != null) {
                        runOnUiThread {
                            bottomLayout.visibility = View.VISIBLE
                            count = 0
                            bottomProgress.visibility = View.VISIBLE
                            fetchMore(next!!)
                        }
                    }

                } else {
                    this@IngredientListActivity.bottomLayout.visibility = View.GONE
                }
            }
        })
    }
    private fun fetchJson(url: String) {

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("fail")
            }

            override fun onResponse(call: Call, response: Response) {
                count++

                val body = response.body()?.string()

                val gson = GsonBuilder().create()
                val feed = gson.fromJson(body, IngredientList :: class.java)
                for (ingredient in feed.results) {
                    ingredientListRepo.add(ViewModel(ingredient, false))
                }
                println(body)
                if (count == 20) {
                    next = feed.next.toString()
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        ingredientListRecycleView.adapter = IngredientListAdapter(this@IngredientListActivity)
                    }
                } else {
                    feed.next?.let { fetchJson(it) }
                }
            }
        })
    }

    private fun fetchMore(url: String) {
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("fail")
            }

            override fun onResponse(call: Call, response: Response) {
                count++

                val body = response.body()?.string()

                val gson = GsonBuilder().create()
                val feed = gson.fromJson(body, IngredientList :: class.java)
                for (ingredient in feed.results) {
                    ingredientListRepo.add(ViewModel(ingredient, false))
                }
                println(body)
                if (count == 20 || feed.next == null) {
                    next = feed.next.toString()
                    runOnUiThread {
                        bottomLayout.visibility = View.GONE
                        bottomProgress.visibility = View.GONE
                        ingredientListRecycleView.adapter?.notifyDataSetChanged()
                    }
                } else {
                    fetchMore(feed.next)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.ingredient_search_menu, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val item = menu?.findItem(R.id.ingredientSearch)
        val searchView = item?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("",false)
                item.collapseActionView()
                Toast.makeText(this@IngredientListActivity,"looking for $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()){
                    search(newText)
                    ingredientListRecycleView.adapter?.notifyDataSetChanged()
                }
                else{
                    reset()
                    ingredientListRecycleView.adapter?.notifyDataSetChanged()
                }
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun add(viewModel: ViewModel) {
        ingredientListRepo.add(viewModel)
    }

    override fun getList(): MutableList<ViewModel> {
        return ingredientListRepo.getList()
    }

    override fun getSize(): Int {
        return ingredientListRepo.getSize()
    }

    override fun setSelected(position: Int, selected: Boolean) {
        ingredientListRepo.setSelected(position, selected)
    }

    override fun search(search: String) {
        ingredientListRepo.search(search)
    }

    override fun reset() {
        ingredientListRepo.reset()
    }

//    private fun search(search: String) {
//        ingredients.clear()
//        for (ingredient in fullIngredient) {
//            if (ingredient.nutritionList.name?.toLowerCase()?.contains(search.toLowerCase())!!) {
//                ingredients.add(ingredient)
//            }
//        }
//
//        if (ingredients.isNullOrEmpty() && !next.isNullOrEmpty()) {
//            next?.let { fetchMore(it) }
//            Toast.makeText(this, "Loading more...", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    private fun reset() {
//        ingredients.clear()
//        ingredients.addAll(fullIngredient)
//    }
}