package com.example.shopify.repo

import com.example.shopify.products.model.ICollectionProductsRepo

interface RemoteSourceInterface  : IBrands, ProductDetalisInterface,
CollectionProductsInterface, IAllProducts, RegisterUserInterFace, IAddresses,
IAddCustomerAddress ,IOrderList,ISelectedProducts,ICart,IPostOrder,ICollectionProductsRepo {
}