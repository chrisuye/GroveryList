package com.seyoum.christian.grocerylist.ingredientList.network.model

import com.google.gson.annotations.SerializedName

data class NutritionModel(

    @SerializedName("name")
    val name: String,
    @SerializedName("energy")
    val energy: String,
    @SerializedName("protein")
    val protein: String,
    @SerializedName("carbohydrates")
    val carbohydrates: String,
    @SerializedName("carbohydrates_sugar")
    val carbohydrates_sugar: String,
    @SerializedName("fat")
    val fat: String,
    @SerializedName("fat_saturated")
    val fat_saturated: String,
    @SerializedName("fibres")
    val fibres: String,
    @SerializedName("sodium")
    val sodium: String
)