package com.thomas.apps.testpaging.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface OfficeService {

    //@Headers("X-API-Key: 2fbbbb10")
    @GET("offices")
    suspend fun requestOffice(
        @Query("_page") page: Int,
//        @Query("size") size: Int
        @Query("_limit") size: Int
    ): List<OfficeResponseItem>

    companion object {
        private const val URL = "https://my.api.mockaroo.com/"
        private const val URL2 = "https://my-json-server.typicode.com/Tho391/b-map/"

        private val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        private val client: OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

        fun create(): OfficeService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(URL2)
                .client(client)
                .build()

            return retrofit.create(OfficeService::class.java)
        }
    }
}