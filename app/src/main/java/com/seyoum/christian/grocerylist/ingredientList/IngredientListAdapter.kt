package com.seyoum.christian.grocerylist.ingredientList

import android.app.AlertDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.seyoum.christian.grocerylist.R
import com.seyoum.christian.grocerylist.ingredientList.interfaces.IIngredientListControl
import com.seyoum.christian.grocerylist.ingredientList.network.model.NutritionList
import com.seyoum.christian.grocerylist.ingredientList.network.model.ViewModel
import kotlinx.android.synthetic.main.list_ingredient.view.*


class IngredientListAdapter(
    private val iIngredientListControl: IIngredientListControl
): RecyclerView.Adapter<GroceryListHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_ingredient,
            parent,
            false
        )
        val viewHolder = GroceryListHolder(view)

        view.infoIngredientBtn.setOnClickListener {
            val position = viewHolder.adapterPosition
            val ingredient = iIngredientListControl.getList()[position]
            val dialogBuilder = AlertDialog.Builder(view.context)


            dialogBuilder.setMessage(makeString(ingredient.nutritionList))

                .setCancelable(true)


            val alert = dialogBuilder.create()

            alert.setTitle(ingredient.nutritionList.name)

            alert.show()
        }

        view.setOnClickListener {
            val position = viewHolder.adapterPosition
            iIngredientListControl.setSelected(position, !iIngredientListControl.getList()[position].selected)
            notifyDataSetChanged()
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: GroceryListHolder, position: Int) {
        holder.bindItem(iIngredientListControl.getList()[position])
    }

    override fun getItemCount(): Int {
        return iIngredientListControl.getSize()
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

class GroceryListHolder(view: View): RecyclerView.ViewHolder(view) {
    fun bindItem(ingredient: ViewModel){
        itemView.ingredient.text = ingredient.nutritionList.name
        if (ingredient.selected) {
            itemView.ingredientCard.setBackgroundColor(Color.LTGRAY)
        } else {
            itemView.ingredientCard.setBackgroundColor(Color.WHITE)
        }
    }

}