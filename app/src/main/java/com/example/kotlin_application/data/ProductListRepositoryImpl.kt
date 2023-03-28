package com.example.kotlin_application.data

import androidx.compose.runtime.snapshots.SnapshotApplyResult
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductListRepositoryImpl @Inject constructor(
    private var productsQuery: Query
): ProductListRepository{
    override fun getProductListFromFirestore(searchText: String) = callbackFlow {
        if (searchText.isNotEmpty()){
            productsQuery = productsQuery.startAt(searchText).endAt("$searchText\uf8ff")
    }
        val snapshotListener = productsQuery.addSnapshotListener { snapshot, e ->
            val response = if (snapshot != null) {
                val productList = snapshot.toObjects(Forum::class.java)
                Response.Success(productList)
            }else{
                Response.Error(e?.message ?: e.toString())
            }
            trySend(response).isSuccess
        }
        awaitClose{
            snapshotListener.remove()
        }
    }
}