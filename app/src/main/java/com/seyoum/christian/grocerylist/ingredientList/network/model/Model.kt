package com.seyoum.christian.grocerylist.ingredientList.network.model


data class IngredientList(val next: String?, val results: List<NutritionList>)

data class NutritionList(
    val name: String?,
    val energy: String?,
    val protein: String?,
    val carbohydrates: String?,
    val carbohydrates_sugar: String?,
    val fat: String?,
    val fat_saturated: String?,
    val fibres: String?,
    val sodium: String?,
    var checked: Boolean = false)