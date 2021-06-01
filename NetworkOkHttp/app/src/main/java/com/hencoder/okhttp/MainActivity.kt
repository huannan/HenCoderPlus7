package com.hencoder.okhttp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import java.net.InetAddress


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://api.github.com/users/rengwuxian/repos"
        val hostname = "api.github.com"

        val client = OkHttpClient.Builder()
            .authenticator(object : Authenticator {
                override fun authenticate(route: Route?, response: Response): Request? {
                    // 刷新token
                    return response.request.newBuilder()
                        .header("Authorization", "Bearer aafesdughnouanoango")
                        .build()
                }
            })
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return emptyList()
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {

                }
            })
            .dns(object : Dns {
                override fun lookup(hostname: String): List<InetAddress> {
                    return emptyList()
                }
            })
            .build()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    println("Response status code: ${response.code}")
                }
            })
    }
}