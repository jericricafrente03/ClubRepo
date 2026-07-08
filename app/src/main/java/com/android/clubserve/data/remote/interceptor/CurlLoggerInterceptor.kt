package com.android.clubserve.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.net.URLDecoder
import java.nio.charset.Charset

class CurlLoggerInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = generateCurlCommand(request)
        CurlPrinter.print("CURL", request.url.toString(), builder)
        return try {
            chain.proceed(request)
        } catch (e: Exception) {
            chain.proceed(chain.request())
        }
    }

    private fun generateCurlCommand(request: Request): String {
        val builder = StringBuilder("")
        builder.apply {
            // command
            append("cURL -g ")
            append("-X ")

            try {
                // method
                append("${request.method.uppercase()} ")
                // header
                request.headers.names().forEach {
                    appendHeader(
                        it,
                        request.headers[it],
                    )
                }
                // request body
                val requestBody = request.body
                appendRequestBody(requestBody)
                // request url
                appendUrl(request)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return builder.toString()
    }

    private fun StringBuilder.appendHeader(key: String, value: String?) {
        this.append("""-H "$key: $value" """)
    }

    private fun StringBuilder.appendRequestBody(body: RequestBody?) {
        body?.let {
            val buffer = Buffer()
            var charset = Charset.forName("UTF-8")
            body.writeTo(buffer)
            val contentType = body.contentType()
            contentType?.let { content ->
                this.appendHeader("Content-Type", content.toString())
                charset = content.charset(Charset.forName("UTF-8"))
                this.append(""" -d '${decode(buffer.readString(charset))}' """)
            }
        }
    }

    private fun StringBuilder.appendUrl(request: Request) {
        val body = decode(request.url.toString())
        this.append(""""$body"""")
        this.append(" -L")
    }

    private fun decode(url: String): String = URLDecoder.decode(url, "UTF-8")
}
