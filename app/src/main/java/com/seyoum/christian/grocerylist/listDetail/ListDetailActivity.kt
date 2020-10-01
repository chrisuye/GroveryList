package com.seyoum.christian.grocerylist.listDetail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.groceryList.GroceryListActivity
import com.seyoum.christian.grocerylist.ingredientList.network.model.NutritionList
import com.seyoum.christian.grocerylist.listDetail.interfaces.IListDetailControl
import kotlinx.android.synthetic.main.activity_list_detail.*
import java.lang.reflect.Type

class ListDetailActivity : AppCompatActivity(), IListDetailControl{
    private var ingredientList: ArrayList<String> = arrayListOf()
    private val nutritionList: MutableList<NutritionList> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail)
        val list = intent.getStringExtra(GroceryListActivity.LAUNCH_DETAIL_LIST)
        val listType: Type = object : TypeToken<ArrayList<String>>() {}.type
        ingredientList =
            Gson().fromJson(
                list,
                listType)

        for (ingredient in ingredientList) {
            nutritionList.add(
                Gson().fromJson(
                ingredient,
                NutritionList::class.java
            ))
        }

        listDetailRecycler.layoutManager = LinearLayoutManager(this)
        listDetailRecycler.adapter = ListDetailAdapter(nutritionList, this)
    }

    override fun onBackPressed() {
        val selectedListString: ArrayList<String> = arrayListOf()
        for (ingredient in nutritionList) {
            selectedListString.add(Gson().toJson(ingredient))
        }

        val changed= Gson().toJson(selectedListString)
        val intent = Intent()
        intent.putExtra(UPDATE_LIST, changed)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        const val UPDATE_LIST = "UPDATE"
    }

    override fun updateChecked(position: Int) {
        nutritionList[position].checked = !nutritionList[position].checked
    }
}