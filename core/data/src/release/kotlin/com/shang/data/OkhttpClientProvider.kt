package com.shang.data

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.Dispatcher
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

class OkHttpClientProvider : OkhttpClientProviderInterface{
    private var dispatcher = Dispatcher()

    override fun getOkHttpClient(pin: String): OkHttpClient.Builder {
        val certificatePinner = CertificatePinner.Builder().add("*.yourdomain.com", pin).build()

        val builder = OkHttpClient.Builder()
        builder.dispatcher(dispatcher)
        builder.certificatePinner(certificatePinner)
        return builder
    }

    override fun cancelAllRequest() {
        dispatcher.cancelAll()
    }
}