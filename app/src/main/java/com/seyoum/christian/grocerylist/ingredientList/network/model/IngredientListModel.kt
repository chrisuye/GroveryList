package com.seyoum.christian.grocerylist.ingredientList.network.model

import com.google.gson.annotations.SerializedName

data class IngredientListModel(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("results")
    val results: List<NutritionModel>
)