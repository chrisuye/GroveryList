package com.seyoum.christian.grocerylist.listDetail

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.ingredientList.network.model.NutritionList
import kotlinx.android.synthetic.main.list_details.view.*

class ListDetailAdapter(
    private val list: MutableList<NutritionList>): RecyclerView.Adapter<ListDetailHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDetailHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_details, parent, false)
        val viewHolder = ListDetailHolder(view)

        view.ingredientInfo.setOnClickListener {
            val position = viewHolder.adapterPosition
            val ingredient = list[position]
            val dialogBuilder = AlertDialog.Builder(view.context)


            dialogBuilder.setMessage(makeString(ingredient))

                .setCancelable(true)


            val alert = dialogBuilder.create()

            alert.setTitle(ingredient.name)

            alert.show()
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ListDetailHolder, position: Int) {
        list[position].name?.let { holder.bindItem(it) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun makeString(nutritionList: NutritionList): String {
        return  "Energy: ${nutritionList.energy}\n"+
                "Protein: ${nutritionList.protein}\n"+
                "Carbohydrates: ${nutritionList.carbohydrates}\n"+
                "Carbohydrates_sugar: ${nutritionList.carbohydrates_sugar}\n"+
                "Fat: ${nutritionList.fat}\n"+
                "Fat_saturated: ${nutritionList.fat_saturated}\n"+
                "Fibres: ${nutritionList.fibres}\n"+
                "Sodium: ${nutritionList.sodium}\n"
    }

}

class ListDetailHolder(view: View): RecyclerView.ViewHolder(view) {
    fun bindItem(ingredient: String){
        itemView.ingreienttext.text = ingredient
    }

}