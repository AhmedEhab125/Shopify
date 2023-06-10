package com.example.shopify.products.model

import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.repo.CollectionProductsInterface
import com.example.shopify.repo.RemoteSource
import kotlinx.coroutines.flow.Flow

class CollectionProductsRepo(var remoteSource: CollectionProductsInterface):CollectionProductsInterface {
    override suspend fun getCollectionProducts(id: Long): Flow<CollectProductsModel?> {
        return remoteSource.getCollectionProducts(id)
    }

}