package com.view.musicplayer.spotifyclone.network.interceptor

import android.content.res.AssetManager
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.BufferedReader
import java.io.InputStreamReader

class AssetFileInterceptor(private val assetManager: AssetManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val segments = chain.request().url.pathSegments

        if (!segments.contains(ASSET_PATH)) {
            return chain.proceed(chain.request())
        }

        val index = segments.indexOf(ASSET_PATH)
        if (index + 1 >= segments.size) {
            return chain.proceed(chain.request())
        }

        val assetFile = segments[index + 1]
        val filePath = "api/$assetFile.json"

        return try {
            val responseBody = readAssetFile(filePath)
                .toResponseBody("application/json;charset=UTF-8".toMediaTypeOrNull())

            Response.Builder()
                .code(200)
                .message("OK")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .body(responseBody)
                .build()
        } catch (e: Exception) {
            chain.proceed(chain.request())
        }
    }

    private fun readAssetFile(path: String): String {
        val reader = BufferedReader(InputStreamReader(assetManager.open(path)))
        val builder = StringBuilder()
        var line: String? = reader.readLine()
        while (line != null) {
            builder.append(line)
            line = reader.readLine()
        }

        return builder.toString()
    }

    companion object {
        private const val ASSET_PATH: String = "assets"
    }
}