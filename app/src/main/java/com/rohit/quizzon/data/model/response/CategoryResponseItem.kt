package com.rohit.quizzon.data.model.response
import com.google.gson.annotations.SerializedName

data class CategoryResponseItem(
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("__createdtime__")
    val createdtime: Long,
    @SerializedName("category_id")
    val id: String,
    @SerializedName("__updatedtime__")
    val updatedtime: Long
) {
    override fun toString(): String {
        return categoryName
    }
}
