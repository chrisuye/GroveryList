package com.seyoum.christian.grocerylist.ingredientList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.ingredientList.network.ServiceBuilder
import com.seyoum.christian.grocerylist.ingredientList.network.model.IngredientList
import com.seyoum.christian.grocerylist.ingredientList.network.model.IngredientListModel
import com.seyoum.christian.grocerylist.ingredientList.network.model.NutritionList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ingredient_list.*
import okhttp3.*
import java.io.IOException

class IngredientListActivity : AppCompatActivity() {
    private var count = 0
    private val ingredients: MutableList<NutritionList> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient_list)

        ingredientListRecycleView.layoutManager = LinearLayoutManager(this)
        val url = "https://wger.de/api/v2/ingredient/?format=json"
        fetchJson(url)
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
                val homeFeed = gson.fromJson(body, IngredientList :: class.java)
                ingredients.addAll(homeFeed.results)
                println(body)
                if (count == 20) {
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        ingredientListRecycleView.adapter = IngredientListAdapter(ingredients)
                    }
                } else {
                    homeFeed.next?.let { fetchJson(it) }
                }
            }
        })
    }
}