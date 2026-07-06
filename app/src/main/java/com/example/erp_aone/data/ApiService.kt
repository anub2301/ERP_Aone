package com.example.erp_aone.data

import retrofit2.http.GET

interface ApiService {
    @GET("status")
    suspend fun checkStatus(): String
}