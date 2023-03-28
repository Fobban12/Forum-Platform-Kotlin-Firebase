package com.example.kotlin_application.data

//There is a single operation that we have to do, so we’ll add it to a repository interface

import kotlinx.coroutines.flow.Flow

typealias ProductList = List<Forum>
typealias ProductListResponse = Response<ProductList>

interface ProductListRepository {
    fun getProductListFromFirestore(searchText:String): Flow<ProductListResponse>
}