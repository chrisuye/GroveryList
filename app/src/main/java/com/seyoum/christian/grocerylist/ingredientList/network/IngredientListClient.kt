package com.seyoum.christian.grocerylist.ingredientList.network

import com.seyoum.christian.grocerylist.ingredientList.network.model.IngredientListModel
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET

interface IngredientListClient {
    @GET("/?format=json")
    fun getIngredients(): Flowable<IngredientListModel>
}