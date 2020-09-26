package com.seyoum.christian.grocerylist.groceryList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.groceryList.interfaces.IGroceryListControl
import kotlinx.android.synthetic.main.list_title.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GroceryListAdapter (private val groceryListControl: IGroceryListControl, val count: Int): RecyclerView.Adapter<GroceryListHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_title, parent, false)
        return GroceryListHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryListHolder, position: Int) {
        holder.bindItem("title")
    }

    override fun getItemCount(): Int {
        return count
    }
}

class GroceryListHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bindItem(string: String){
        itemView.title.text = string
    }

}