package com.umair.helpingout.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.umair.helpingout.internal.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class ConnectivityInterceptorImp(context: Context) : ConnectivityInterceptor{

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
            if(!isOnline() )
                throw NoConnectivityException()

            //if(!isHostReachable())
              //  throw ServerDownException()

        return chain.proceed(chain.request())
    }

    private fun isOnline() : Boolean{

        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
        as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (connectivityManager.activeNetwork != null) {
                    val nc = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork);

                    return (nc != null && (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)))
                }
        } else {
                val networkInfo = connectivityManager.allNetworkInfo
                for ( tempNetworkInfo in networkInfo) {
                    if (tempNetworkInfo.isConnected) {
                        return true
                    }
                }
        }

        return false
    }



    private fun isServerOnline() : Boolean{
        val runtime = Runtime.getRuntime()
        val proc =
            runtime.exec("ping -c 1 https://helpingout.myandroid.app") //<- Try ping -c 1 www.serverURL.com

        val mPingResult = proc.waitFor()
        return mPingResult == 0

    }


    fun isHostReachable(
    ): Boolean {
        val cm =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return if (netInfo != null && netInfo.isConnected) {
            try {
                val url =
                    URL("https://helpingout.myandroid.app") // Change to "http://google.com" for www  test.
                val urlc: HttpURLConnection = url.openConnection() as HttpURLConnection
                urlc.connectTimeout = 10 * 1000 // 10 s.
                urlc.connect()
                if (urlc.responseCode === 200) { // 200 = "OK" code (http connection is fine).
                    Log.wtf("Connection", "Success !")
                    true
                } else {
                    false
                }
            } catch (e1: MalformedURLException) {
                false
            } catch (e: IOException) {
                false
            }
        } else false
    }


}