package com.seyoum.christian.grocerylist.ingredientList.network.model


class IngredientList(val next: String?, val results: List<NutritionList>)

class NutritionList(
    val name: String?,
    val energy: String?,
    val protein: String?,
    val carbohydrates: String?,
    val carbohydrates_sugar: String?,
    val fat: String?,
    val fat_saturated: String?,
    val fibres: String?,
    val sodium: String?)