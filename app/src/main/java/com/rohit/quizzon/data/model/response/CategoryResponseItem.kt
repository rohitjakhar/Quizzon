package com.rohit.quizzon.data.model.response

import com.google.gson.annotations.SerializedName
import com.rohit.quizzon.utils.Config.Companion.currentLanguage
import java.util.*

data class CategoryResponseItem(
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("category_name_sa")
    val categoryNameSanskrit: String,
    @SerializedName("__createdtime__")
    val createdtime: Long,
    @SerializedName("category_id")
    val id: String,
    @SerializedName("__updatedtime__")
    val updatedtime: Long
) {
    override fun toString(): String {
        return if (currentLanguage == "en")
            categoryName
        else
            categoryNameSanskrit
    }
}
