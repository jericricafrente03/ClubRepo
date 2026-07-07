package com.android.clubserve.data.remote.api

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*
import com.android.clubserve.data.remote.dto.*

class ClubServeApi {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                prettyPrint = true
            })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }

    suspend fun getHomeData(): List<JsonElement> {
        val url = "https://control.app-preview.sb.clubserve.app/api/trpc/mainCategory.listAll,subCategory.list,groups.list,subCategory.lovedByLocals,bookings.getUserUpcomingBookings?batch=1&input=%7B%223%22:%7B%22json%22:%7B%22limit%22:5,%22page%22:1%7D%7D,%220%22:%7B%22json%22:null%7D,%222%22:%7B%22json%22:null%7D,%224%22:%7B%22json%22:%7B%22limit%22:10,%22page%22:1%7D%7D,%221%22:%7B%22json%22:null%7D%7D"
        val response: HttpResponse = client.get(url)
        return Json.decodeFromString(response.bodyAsText())
    }
}
