package com.seyoum.christian.grocerylist.ingredientList

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.groceryList.interfaces.IGroceryListControl
import com.seyoum.christian.grocerylist.ingredientList.network.model.IngredientList
import com.seyoum.christian.grocerylist.ingredientList.network.model.NutritionList
import kotlinx.android.synthetic.main.list_ingredient.view.*
import kotlinx.android.synthetic.main.list_title.view.*

class IngredientListAdapter(private val ingredientList: List<NutritionList>): RecyclerView.Adapter<GroceryListHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_ingredient, parent, false)
        return GroceryListHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryListHolder, position: Int) {
        ingredientList[position].name?.let { holder.bindItem(it) }
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
}

class GroceryListHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bindItem(ingredient: String){
        itemView.ingredient.text = ingredient
    }

}