package com.qpay.xcash.di

import android.util.Log
import com.qpay.xcash.network.AuthInterceptor
import com.qpay.xcash.network.api.CardApiService
import com.qpay.xcash.network.api.PaymentApiService
import com.qpay.xcash.network.api.PublicApiService
import com.qpay.xcash.network.api.UserApiService
import com.qpay.xcash.network.fake.FakeCardApiService
import com.qpay.xcash.network.fake.FakePaymentApiService
import com.qpay.xcash.network.fake.FakePublicApiService
import com.qpay.xcash.network.fake.FakeUserApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Log.d("API_LOG", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // ==============================================================
    // 1. 公開的 OkHttpClient 與 Retrofit (無 Token)
    // ==============================================================
    @Provides
    @Singleton
    @PublicClient
    fun providePublicOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @PublicClient
    fun providePublicRetrofit(
        @PublicClient okHttpClient: OkHttpClient, 
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun providePublicApiService(@PublicClient retrofit: Retrofit): PublicApiService {
        // 🔴 正式版：
//         return retrofit.create(PublicApiService::class.java)

        // 🟢 假資料開發版：
        return FakePublicApiService()
    }

    // ==============================================================
    // 2. 驗證的 OkHttpClient 與 Retrofit (有 Token)
    // ==============================================================
    @Provides
    @Singleton
    @AuthClient
    fun provideAuthOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // 注入 Token Interceptor
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @AuthClient
    fun provideAuthRetrofit(
        @AuthClient okHttpClient: OkHttpClient, 
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApiService(@AuthClient retrofit: Retrofit): UserApiService {
        // 🔴 正式版：
        // return retrofit.create(UserApiService::class.java)

        // 🟢 假資料開發版：
        return FakeUserApiService()
    }

    @Provides
    @Singleton
    fun providePaymentApiService(@AuthClient retrofit: Retrofit): PaymentApiService {
        // 🔴 正式版：
        // return retrofit.create(PaymentApiService::class.java)

        // 🟢 假資料開發版：
        return FakePaymentApiService()
    }

    @Provides
    @Singleton
    fun provideCardApiService(@AuthClient retrofit: Retrofit): CardApiService {
        // 🔴 正式版：
        // return retrofit.create(CardApiService::class.java)

        // 🟢 假資料開發版：
        return FakeCardApiService()
    }
}
