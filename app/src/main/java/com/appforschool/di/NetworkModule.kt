package com.appforschool.di

import com.appforschool.BuildConfig
import com.appforschool.api.ApiExceptions
import com.appforschool.api.WebServiceInterface
import com.appforschool.utils.Constant
import com.appforschool.utils.LogM
import com.appforschool.utils.PrefUtils
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Network Manger Class all the API Calls will be made by this class
 */
@Module
class NetworkModule {

    /**
     * final variable having Base Server URL of project
     * */
    private val mBaseUrl = BuildConfig.BASE_URL

    /**
     * Gives OkHttpClient
     * @return object of [OkHttpClient]
     * */
    @Singleton
    @Provides
    fun providesOkHttp(prefUtils: PrefUtils): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(RequestTokenInterceptor(prefUtils)).build()

    /**
     * @return Object of [WebServiceInterface]
     * */
    @Singleton
    @Provides
    fun provideApiService(oktHttpClient: OkHttpClient): WebServiceInterface =
        Retrofit.Builder().client(oktHttpClient)
            .baseUrl(mBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WebServiceInterface::class.java)


    /**
     * To Add Authorization token and proceeds with API request
     */
    inner class RequestTokenInterceptor(private val prefUtils: PrefUtils) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val encodedPath = chain.request().url.encodedPath
            var request = chain.request()
            try {
                return if (encodedPath.contains("login") || encodedPath == "/") {
                    chain.proceed(chain.request())
                } else {
                    var request = chain.request()
//                if (prefUtils.getAuthenticationToken().isEmpty().not()) {
//                    request = request.newBuilder()
//                        .addHeader(
//                            Constant.HEADER_AUTHORIZATION,
//                            Constant.TOKEN + " " + prefUtils.getAuthenticationToken()
//                        ).build()
//                }
                    chain.proceed(request)
                }
            } catch (e: SocketTimeoutException) {
                throw ApiExceptions(Constant.SOCKET_TIMEOUT_EXCEPTIONS)
            } catch (e: Exception) {
                throw ApiExceptions(e.message!!)
            }
            return chain.proceed(request)
        }
    }
}