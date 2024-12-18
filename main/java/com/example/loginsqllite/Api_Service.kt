package com.example.loginsqllite

import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.Part

interface Api_Service {

    @Multipart
    @POST("/predict_image/")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("desc") desc: Nothing?
    ):Call<UploadResponse>


    companion object {
        operator fun invoke(): Api_Service{
            return Retrofit.Builder()
                .baseUrl("https://1154-78-171-229-239.ngrok-free.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api_Service::class.java)

        }
    }



}