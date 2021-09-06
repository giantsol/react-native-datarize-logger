package com.reactnativedatarizelogger

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

internal object ApiServer {
  private const val TIMEOUT = 3000L

  val service: Service = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("https://applog-dev.datarize.ai/")
    .client(createOkHttpClient())
    .build()
    .create(Service::class.java)

  private fun createOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()
      .callTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
      .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
      .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
      .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)

    if (BuildConfig.DEBUG) {
      builder.addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
      })
    }

    return builder.build()
  }

  interface Service {
    @POST("99999/testapp/log")
    suspend fun clickLog(@Body body: ClickLogEntity)
  }
}
