package com.seyoum.christian.grocerylist.ingredientList.interfaces

import com.seyoum.christian.grocerylist.ingredientList.network.model.NutritionList
import com.seyoum.christian.grocerylist.ingredientList.network.model.ViewModel


interface IIngredientListRepo {
    fun add(viewModel: ViewModel)
    fun getList(): MutableList<ViewModel>
    fun getSize(): Int
    fun setSelected(position: Int, selected: Boolean)
    fun getSelectedSize(): Int
    fun clearSelected()
    fun getSelectedList(): MutableList<NutritionList>
    fun search(search: String)
    fun reset()
}