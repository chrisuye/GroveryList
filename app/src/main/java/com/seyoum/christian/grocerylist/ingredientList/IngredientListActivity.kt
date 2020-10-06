package com.seyoum.christian.grocerylist.ingredientList

import android.app.Activity
import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
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
    private lateinit var title: EditText
    private var isSearching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient_list)

        ingredientListRecycleView.layoutManager = LinearLayoutManager(this)
        val url = "https://wger.de/api/v2/ingredient/?format=json"
        fetchJson(url)

        inputTitle()
        ingredientListRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!ingredientListRecycleView.canScrollVertically(1) && !isSearching) {
                    if (next != null) {
                        runOnUiThread {
                            Toast.makeText(this@IngredientListActivity, "Loading...", Toast.LENGTH_LONG).show()
                            count = 0
                            fetchMore(next!!)
                        }
                    }
                }
            }
        })

        clearSavedBtn.setOnClickListener {
            bottomLayout.visibility = View.GONE
            ingredientListRepo.clearSelected()
            ingredientListRecycleView.adapter?.notifyDataSetChanged()
        }

        saveBtn.setOnClickListener {
            val selectedListString: ArrayList<String> = arrayListOf()
            for (ingredient in ingredientListRepo.getSelectedList()) {
                selectedListString.add(Gson().toJson(ingredient))
            }
            val selected = Gson().toJson(selectedListString)
            val intent = Intent()
            intent.putExtra(ADD_LIST_EXTRA_KEY, selected)
            intent.putExtra(ADD_TITLE_EXTRA_KEY, title.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
    private fun fetchJson(url: String) {

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("fail")
            }

            override fun onResponse(call: Call, response: Response) {
                count++

                val body = response.body()?.string()

                val gson = GsonBuilder().create()
                val feed = gson.fromJson(body, IngredientList::class.java)
                for (ingredient in feed.results) {
                    ingredientListRepo.add(ViewModel(ingredient, false))
                }
                println(body)
                if (count == 20) {
                    next = feed.next.toString()
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        ingredientListRecycleView.adapter =
                            IngredientListAdapter(this@IngredientListActivity)
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

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("fail")
            }

            override fun onResponse(call: Call, response: Response) {
                count++

                val body = response.body()?.string()

                val gson = GsonBuilder().create()
                val feed = gson.fromJson(body, IngredientList::class.java)
                for (ingredient in feed.results) {
                    ingredientListRepo.add(ViewModel(ingredient, false))
                }
                println(body)
                if (count == 20 || feed.next == null) {
                    next = feed.next.toString()
                    runOnUiThread {
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
                searchView.setQuery("", false)
                item.collapseActionView()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    isSearching = true
                    search(newText)
                    ingredientListRecycleView.adapter?.notifyDataSetChanged()
                } else {
                    isSearching = false
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
        if (ingredientListRepo.getSelectedSize() > 0) {
            bottomLayout.visibility = View.VISIBLE
            savedCountText.text = "${ingredientListRepo.getSelectedSize()} Selected"
        } else {
            bottomLayout.visibility = View.GONE
        }
    }

    override fun search(search: String) {
        ingredientListRepo.search(search)
    }

    override fun reset() {
        ingredientListRepo.reset()
    }

    private fun inputTitle() {
        val view = LayoutInflater.from(this).inflate(R.layout.input_title, null)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(view)
        title = view.findViewById(R.id.titleInputText)
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Next") { _, _ ->

                Toast.makeText(this, title.text.toString(), Toast.LENGTH_LONG).show()
            }

            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
                onBackPressed()
            }


        val alert = dialogBuilder.create()

        alert.setTitle("Input Title")

        alert.show()
    }

    companion object{
        const val ADD_LIST_EXTRA_KEY = "ADD_LIST"
        const val ADD_TITLE_EXTRA_KEY = "ADD_TiTLE"
    }
}