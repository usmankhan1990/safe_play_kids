package com.safeplay.kids.network

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.security.MessageDigest

class AndroidHeaderInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val packageName = context.packageName
        val sha1 = context.getSha1Signature()

        val request = chain.request().newBuilder()
            .addHeader("X-Android-Package", packageName)
            .addHeader("X-Android-Cert", sha1)
            .build()

        // Log full request URL + headers
        Log.d("Interceptor", "→ ${request.method} ${request.url}")
        Log.d("Interceptor", "X-Android-Package: $packageName")
        Log.d("Interceptor", "X-Android-Cert: $sha1")

        return chain.proceed(request)
    }


    private fun Context.getSha1Signature(): String {
        val sig = packageManager
            .getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            .signingInfo?.apkContentsSigners?.firstOrNull()
            ?: return ""

        val digest = MessageDigest.getInstance("SHA1").digest(sig.toByteArray())
        // Google expects **uppercase hex w/o colons**
        return digest.joinToString("") { "%02X".format(it) }
    }


}
