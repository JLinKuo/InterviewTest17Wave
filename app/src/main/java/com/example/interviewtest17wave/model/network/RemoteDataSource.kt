package com.example.mvvmcodebase.model.network

import android.util.Log
import com.example.interviewtest17wave.BuildConfig.API_BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import retrofit2.converter.moshi.MoshiConverterFactory

class RemoteDataSource {

    private val TAG = javaClass.name

    private val isShowRetrofitLogs = true
    private val defaultTimeout = 30L

    private val httpHeader = Interceptor { chain ->
        val builder = chain.request().newBuilder()
        builder.header("Authorization", "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")
               .header("Accept", "application/vnd.github.v3.text-match+json")
        return@Interceptor chain.proceed(builder.build())
    }

    private val logging = HttpLoggingInterceptor { message -> Log.d(TAG, message) }

    private val okHttpClient: OkHttpClient by lazy {
        if(isShowRetrofitLogs) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        OkHttpClient.Builder()
            .connectTimeout(defaultTimeout, TimeUnit.SECONDS)
            .readTimeout(defaultTimeout, TimeUnit.SECONDS)
            .writeTimeout(defaultTimeout, TimeUnit.SECONDS)
            .addInterceptor(httpHeader)
            .addInterceptor(logging)
            .build()
    }

    private val moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    fun <Api> buildApi(
        api: Class<Api>
    ): Api {
        return Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())      // 透過Multipart傳檔案Server，Server那的Json格式都會夾帶雙引號
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()
                    .create(api)
    }
}