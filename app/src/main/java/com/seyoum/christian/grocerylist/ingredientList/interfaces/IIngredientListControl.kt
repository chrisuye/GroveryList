package com.seyoum.christian.grocerylist.ingredientList.interfaces

import com.seyoum.christian.grocerylist.ingredientList.network.model.ViewModel

interface IIngredientListControl {
    fun add(viewModel: ViewModel)
    fun getList(): MutableList<ViewModel>
    fun getSize(): Int
    fun setSelected(position: Int, selected: Boolean)
    fun search(search: String)
    fun reset()
}