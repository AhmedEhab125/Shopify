package com.example.shopify.products.model

import com.example.shopify.Models.products.CollectProductsModel
import com.example.shopify.repo.CollectionProductsInterface
import com.example.shopify.repo.ISelectedProducts
import com.example.shopify.repo.RemoteSource
import com.example.shopify.repo.RemoteSourceInterface
import kotlinx.coroutines.flow.Flow

class CollectionProductsRepo(var remoteSource: RemoteSourceInterface ): ICollectionProductsRepo {
    override suspend fun getCollectionProducts(id: Long): Flow<CollectProductsModel?> {
        return remoteSource.getCollectionProducts(id)
    }

    override suspend fun getSelectedProducts(ids: String): Flow<CollectProductsModel?> {
      return remoteSource.getSelectedProducts(ids)
    }

}