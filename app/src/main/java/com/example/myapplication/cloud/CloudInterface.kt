package com.example.myapplication.cloud

import com.example.myapplication.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*

interface CloudInterface {
    @GET("importNotes")
    suspend fun importNotes(

        @Query("userName") userName: String,
        @Query("phoneId") phoneId: String
    ): Response<List<CloudNote>>


    @POST("exportNotes")
    suspend fun exportNotes(@Body body: ExportNotesRequestBody): Response<Any>

    companion object {
        private const val API_URL = "https://us-central1-plannerapi-2d0bf.cloudfunctions.net"

        fun get(): CloudInterface = Retrofit.Builder().baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    }
                }.build()
            )
            .build().create()
    }

}