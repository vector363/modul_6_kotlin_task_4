package com.example.data.service

import com.example.domain.model.Laureate
import com.example.domain.model.Prize
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ApiNobelPrize(
    @SerialName("awardYear")
    val awardYear: String,
    @SerialName("category")
    val category: Category,
    @SerialName("prizeAmount")
    val prizeAmount: Int?,
    @SerialName("dateAwarded")
    val dateAwarded: String?,
    @SerialName("laureates")
    val laureates: List<ApiLaureate>? = null  // ← сделал nullable
)

@Serializable
data class Category(
    @SerialName("en")
    val en: String
)

@Serializable
data class ApiLaureate(
    @SerialName("id")
    val id: String,
    @SerialName("fullName")
    val fullName: FullName? = null,  // ← nullable
    @SerialName("knownName")
    val knownName: KnownName? = null,  // ← добавил knownName
    @SerialName("orgName")
    val orgName: OrgName? = null,  // ← добавил для организаций
    @SerialName("motivation")
    val motivation: Motivation? = null,
    @SerialName("portion")
    val portion: String,
    @SerialName("birth")
    val birth: Birth? = null  // ← сделал nullable
)

@Serializable
data class FullName(
    @SerialName("en")
    val en: String? = null
)

@Serializable
data class KnownName(
    @SerialName("en")
    val en: String? = null
)

@Serializable
data class OrgName(
    @SerialName("en")
    val en: String? = null
)

@Serializable
data class Motivation(
    @SerialName("en")
    val en: String? = null
)

@Serializable
data class Birth(
    @SerialName("date")
    val date: String? = null,
    @SerialName("place")
    val place: Place? = null
)

@Serializable
data class Place(
    @SerialName("city")
    val city: String? = null,
    @SerialName("country")
    val country: String? = null
)

object NobelPrizeApiService {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun fetchPrizes(limit: Int = 50): List<ApiNobelPrize> {
        val response = client.get("https://api.nobelprize.org/2.1/nobelPrizes?limit=$limit")
        val jsonResponse = response.body<String>()
        val json = Json { ignoreUnknownKeys = true }
        val wrapper = json.decodeFromString<NobelPrizesResponse>(jsonResponse)
        return wrapper.nobelPrizes
    }

    @Serializable
    data class NobelPrizesResponse(
        @SerialName("nobelPrizes")
        val nobelPrizes: List<ApiNobelPrize>
    )

    fun mapToDomain(apiPrize: ApiNobelPrize): Prize {
        return Prize(
            id = 0,
            awardYear = apiPrize.awardYear,
            category = apiPrize.category.en.lowercase(),
            prizeAmount = apiPrize.prizeAmount,
            dateAwarded = apiPrize.dateAwarded,
            laureates = apiPrize.laureates?.map { apiLaureate ->
                // Получаем имя из доступных полей
                val name = when {
                    apiLaureate.fullName?.en != null -> apiLaureate.fullName.en
                    apiLaureate.knownName?.en != null -> apiLaureate.knownName.en
                    apiLaureate.orgName?.en != null -> apiLaureate.orgName.en
                    else -> "Unknown"
                }

                // Получаем мотивацию
                val motivation = apiLaureate.motivation?.en ?: "No description"

                // Формируем место рождения
                val birthPlace = apiLaureate.birth?.place?.let {
                    listOfNotNull(it.city, it.country).joinToString(", ")
                } ?: "Unknown"

                Laureate(
                    id = apiLaureate.id,
                    fullName = name,
                    motivation = motivation,
                    portion = apiLaureate.portion,
                    birthDate = apiLaureate.birth?.date,
                    birthPlace = birthPlace
                )
            } ?: emptyList()
        )
    }
}