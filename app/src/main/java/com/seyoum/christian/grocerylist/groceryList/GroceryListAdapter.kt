package com.seyoum.christian.grocerylist.groceryList

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.groceryList.data.GroceryListEntity
import com.seyoum.christian.grocerylist.groceryList.interfaces.IGroceryListControl
import com.seyoum.christian.grocerylist.ingredientList.network.model.NutritionList
import kotlinx.android.synthetic.main.list_title.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class GroceryListAdapter (
    private val groceryListControl: IGroceryListControl,
    private val count: Int,
    private val userName: String): RecyclerView.Adapter<GroceryListHolder>(){

    private var energy  = 0.0
    private var protein  = 0.0
    private var carbohydrates  = 0.0
    private var carbohydrates_sugar  = 0.0
    private var fat  = 0.0
    private var fat_saturated  = 0.0
    private var fibres  = 0.0
    private var sodium  = 0.0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListHolder {
        var list = GroceryListEntity("","",userName)
        var check = true
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_title, parent, false)
        val viewHolder = GroceryListHolder(view)
        view.groceryInfoBtn.setOnClickListener {
            val groceryList: MutableList<NutritionList> = mutableListOf()
            var  ingredientList: ArrayList<String> = arrayListOf()
            reset()
            val position = viewHolder.adapterPosition
            GlobalScope.launch {
                list = groceryListControl.getGroceryList(userName)[position]
                val listType: Type = object : TypeToken<ArrayList<String>>() {}.type
                ingredientList =
                    Gson().fromJson(
                        list.ingredient,
                        listType)
                check = false
            }
            while (check){
                groceryListControl.showLoading(check)
            }
            groceryListControl.showLoading(check)
            for (ingredient in ingredientList) {
                groceryList.add(Gson().fromJson(
                    ingredient,
                    NutritionList::class.java
                ))
            }
            for (grocery in groceryList) {
                energy += grocery.energy?.toDouble() ?: 0.0
                protein  += grocery.protein?.toDouble() ?: 0.0
                carbohydrates  += grocery.carbohydrates?.toDouble() ?: 0.0
                carbohydrates_sugar  += grocery.carbohydrates_sugar?.toDouble() ?: 0.0
                fat += grocery.fat?.toDouble() ?: 0.0
                fat_saturated += grocery.fat_saturated?.toDouble() ?: 0.0
                fibres += grocery.fibres?.toDouble() ?: 0.0
                sodium += grocery.sodium?.toDouble() ?: 0.0
            }

            val dialogBuilder = AlertDialog.Builder(view.context)


            dialogBuilder.setMessage(makeString())

                .setCancelable(true)


            val alert = dialogBuilder.create()

            alert.setTitle(list.title)

            alert.show()

        }

        view.setOnClickListener {
            val position = viewHolder.adapterPosition
            check = true
            GlobalScope.launch {
                list = groceryListControl.getGroceryList(userName)[position]
                check = false
            }
            while (check){
                groceryListControl.showLoading(check)
            }
            groceryListControl.showLoading(check)
            groceryListControl.launchListDetail(list.ingredient)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: GroceryListHolder, position: Int) {
        var title = ""
        GlobalScope.launch {
            title = groceryListControl.getGroceryList(userName)[position].title
        }
        while (title == "") {
            groceryListControl.showLoading(true)
        }
        groceryListControl.showLoading(false)
        holder.bindItem(title)
    }

    override fun getItemCount(): Int {
        return count
    }

    private fun makeString(): String {
        return  "Total Energy: ${energy}\n"+
                "Total Protein: ${protein}\n"+
                "Total Carbohydrates: ${carbohydrates}\n"+
                "Total Carbohydrates_sugar: ${carbohydrates_sugar}\n"+
                "Total Fat: ${fat}\n"+
                "Total Fat_saturated: ${fat_saturated}\n"+
                "Total Fibres: ${fibres}\n"+
                "Total Sodium: ${sodium}\n"
    }

    private fun reset() {
        energy = 0.0
        protein  = 0.0
        carbohydrates  = 0.0
        carbohydrates_sugar  = 0.0
        fat = 0.0
        fat_saturated = 0.0
        fibres = 0.0
        sodium = 0.0
    }
}

class GroceryListHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bindItem(title: String){
        itemView.title.text = title
    }

}