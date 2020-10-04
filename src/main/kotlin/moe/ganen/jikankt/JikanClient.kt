package moe.ganen.jikankt

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import moe.ganen.jikankt.connection.RateLimitInterceptor
import moe.ganen.jikankt.utils.JikanLogger
import okhttp3.Protocol
import org.slf4j.Logger

open class JikanClient {
    var httpClient = HttpClient(OkHttp) {
        engine {
            config {
                protocols(listOf(Protocol.HTTP_1_1))
                //see: https://github.com/ktorio/ktor/issues/1708
                retryOnConnectionFailure(true)
            }
            addInterceptor(RateLimitInterceptor())
        }

        install(JsonFeature) {
            serializer = GsonSerializer()
        }

        expectSuccess = false
    }

    companion object {
        const val JIKANKT_NAME = "JikanKt"
        const val JIKANKT_VERSION = "1.3.2"
        val JIKANKT_LOG: Logger = JikanLogger().getLog(JIKANKT_NAME)
    }
}