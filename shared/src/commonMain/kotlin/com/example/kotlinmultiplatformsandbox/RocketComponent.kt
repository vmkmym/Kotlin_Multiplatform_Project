package com.example.kotlinmultiplatformsandbox

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json


class RocketComponent {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    //  마지막으로 성공한 실행을 찾는 함수
    // Suspending functions can only be called from coroutines or other suspending functions. This is why getDateOfLastSuccessfulLaunch() was marked with the suspend keyword. The network request is executed in the HTTP client's thread pool.
    private suspend fun getDateOfLastSuccessfulLaunch(): String {
        val rockets: List<RocketLaunch> =
            httpClient.get("https://api.spacexdata.com/v4/launches").body()
        val lastSuccessLaunch = rockets.last { it.launchSuccess == true }
        val date = Instant.parse(lastSuccessLaunch.launchDateUTC)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        return "${date.month} ${date.dayOfMonth}, ${date.year}"
    }

    // 메시지 생성 일시정지 함수
    suspend fun launchPhrase(): String =
        try {
            "The last successful launch was on ${getDateOfLastSuccessfulLaunch()} 🚀"
        } catch (e: Exception) {
            println("Exception during getting the date of the last successful launch $e")
            "Error occurred"
        }
}


// 여기서 JSON 직렬화 도구는 prettyPrint 속성을 통해 JSON을 더 읽기 쉽게 출력하도록 설정되어 있습니다.
// isLenient를 사용하여 형식이 잘못된 JSON을 더 유연하게 읽을 수 있습니다.
// 또한, 로켓 발사 모델에서 선언되지 않은 키는 ignoreUnknownKeys를 통해 무시합니다.