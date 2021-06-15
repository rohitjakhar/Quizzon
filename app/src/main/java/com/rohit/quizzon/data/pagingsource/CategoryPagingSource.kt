package com.rohit.quizzon.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rohit.quizzon.data.QuizService
import com.rohit.quizzon.data.model.body.CategoryBody
import com.rohit.quizzon.data.model.response.CategoryResponseItem

private const val INITIAL_START_PAGE = 1

class CategoryPagingSource(
    private val apiCall: QuizService,
    private val categoryBody: CategoryBody,
    private val operationToken: String
) : PagingSource<Int, CategoryResponseItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryResponseItem> {
        return try {
            val response = apiCall.fetchCategory("Bearer $operationToken", categoryBody)
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CategoryResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition((anchorPosition))
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
