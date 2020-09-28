package com.seyoum.christian.grocerylist.ingredientList

import com.seyoum.christian.grocerylist.ingredientList.interfaces.IIngredientListRepo
import com.seyoum.christian.grocerylist.ingredientList.network.model.NutritionList
import com.seyoum.christian.grocerylist.ingredientList.network.model.ViewModel

class IngredientListRepo: IIngredientListRepo{
    private val fullIngredientList: MutableList<ViewModel> = mutableListOf()
    private val ingredientList: MutableList<ViewModel> = mutableListOf()
    private val selectedIngredientList: MutableList<NutritionList> = mutableListOf()

    override fun add(viewModel: ViewModel) {
        fullIngredientList.add(viewModel)
        ingredientList.add(viewModel)
    }

    override fun getList(): MutableList<ViewModel> {
        return ingredientList
    }

    override fun getSize(): Int {
        return ingredientList.size
    }

    override fun setSelected(position: Int, selected: Boolean) {
        ingredientList[position].selected = selected
        fullIngredientList[position].selected = selected
        if (selected)
            addToList(ingredientList[position])
        else
            removeFromList(ingredientList[position])
    }

    override fun search(search: String) {
        ingredientList.clear()
        for (ingredient in fullIngredientList) {
            if (ingredient.nutritionList.name?.toLowerCase()?.contains(search.toLowerCase())!!) {
                ingredientList.add(ingredient)
            }
        }

//        if (ingredientList.isNullOrEmpty() && !next.isNullOrEmpty()) {
//            next?.let { fetchMore(it) }
//            Toast.makeText(this, "Loading more...", Toast.LENGTH_LONG).show()
//        }
    }

    override fun reset() {
        ingredientList.clear()
        ingredientList.addAll(fullIngredientList)
    }

    private fun addToList(viewModel: ViewModel) {
        selectedIngredientList.add(viewModel.nutritionList)
    }

    private fun removeFromList(viewModel: ViewModel) {
        selectedIngredientList.remove(viewModel.nutritionList)
    }
}