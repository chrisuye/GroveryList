package com.seyoum.christian.grocerylist.listDetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.groceryList.GroceryListActivity
import com.seyoum.christian.grocerylist.ingredientList.network.model.NutritionList
import kotlinx.android.synthetic.main.activity_list_detail.*
import java.lang.reflect.Type

class ListDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail)
        val groceryList: MutableList<NutritionList> = mutableListOf()
        val ingredientList: ArrayList<String>
        val list = intent.getStringExtra(GroceryListActivity.LAUNCH_DETAIL_LIST)
        val listType: Type = object : TypeToken<ArrayList<String>>() {}.type
        ingredientList =
            Gson().fromJson(
                list,
                listType)

        for (ingredient in ingredientList) {
            groceryList.add(
                Gson().fromJson(
                ingredient,
                NutritionList::class.java
            ))
        }

        listDetailRecycler.layoutManager = LinearLayoutManager(this)
        listDetailRecycler.adapter = ListDetailAdapter(groceryList)
    }
}