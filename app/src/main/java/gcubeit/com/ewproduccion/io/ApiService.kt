package gcubeit.com.ewproduccion.io

import gcubeit.com.ewproduccion.io.response.LoginResponse
import gcubeit.com.ewproduccion.io.response.SimpleResponse
import gcubeit.com.ewproduccion.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @GET("codes")
    abstract fun getCodes(): Call<ArrayList<Code>>

    @GET("machines")
    abstract fun getMachines(): Call<ArrayList<Machine>>

    @GET("products")
    abstract fun getProducts(): Call<ArrayList<Product>>

    @GET("colors")
    abstract fun getColors(): Call<ArrayList<Color>>

    @POST("login")
    fun postLogin(@Query("username") username: String, @Query("password") password: String):
            Call<LoginResponse>

    @POST("logout")
    fun postLogout(@Header("Authorization") authHeader: String): Call<Void>

    @GET("stops")
    fun getStops(@Header("Authorization") authHeader: String):
            Call<ArrayList<Stop>>

    @POST("stops")
    @Headers("Accept: application/json")
    fun storeStop(
        @Header("Authorization") authHeader: String,
        @Query("machine_id") machineId: Int,
        @Query("product_id") productId: Int?,
        @Query("color_id") colorId: Int?,
        @Query("code_id") codeId: Int,
        @Query("meters") meters: Int?,
        @Query("comment") comment: String?
    ): Call<SimpleResponse>

    /*@GET("specialties/{specialty}/doctors")
    abstract fun getDoctors(@Path("specialty") specialtyId: Int): Call<ArrayList<Doctor>>*/

    companion object Factory {
        private const val BASE_URL = "http://134.122.113.150/api/"

        fun create(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}