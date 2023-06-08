package com.mihir.swipetask.network

import com.mihir.swipetask.data.ProductsListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface NetworkService {

    @GET("get")
    suspend fun getProductData(): Response<ProductsListResponse>

    @POST("add")
    @FormUrlEncoded
    suspend fun postProductData(
        @Field("product_name") productName:String,
        @Field("product_type") productType:String,
        @Field("price") price:String,
        @Field("tax") tax:String,
    ): Response<Any>

    // multipart request is needed to send image in API
    @POST("add")
    @Multipart
    suspend fun postProductDataWithImage(
        @Part("product_name") productName:RequestBody,
        @Part("product_type") productType:RequestBody,
        @Part("price") price:Double,
        @Part("tax") tax:Double,
        @Part image: MultipartBody.Part
    ): Response<Any>

}