package com.example.data.datasource

import com.example.domain.model.Laureate
import com.example.domain.model.NobelPrize
import com.example.domain.model.User

object InMemoryDataSource {

    val users = listOf(
        User(
            id = 1,
            username = "admin",
            password = "admin123",
            email = "admin@example.com"
        )
    )

    val prizes = listOf(
        NobelPrize(
            year = "1901",
            category = "physics",
            prizeAmount = 150782,
            laureates = listOf(
                Laureate(
                    id = "1",
                    fullName = "Wilhelm Conrad Röntgen",
                    motivation = "in recognition of the extraordinary services he has rendered by the discovery of the remarkable rays subsequently named after him",
                    birthDate = "1845-03-27",
                    birthPlace = "Lennep, Prussia"
                )
            )
        ),
        NobelPrize(
            year = "1901",
            category = "chemistry",
            prizeAmount = 150782,
            laureates = listOf(
                Laureate(
                    id = "2",
                    fullName = "Jacobus Henricus van 't Hoff",
                    motivation = "in recognition of the extraordinary services he has rendered by the discovery of the laws of chemical dynamics and osmotic pressure in solutions",
                    birthDate = "1852-08-30",
                    birthPlace = "Rotterdam, Netherlands"
                )
            )
        ),
        NobelPrize(
            year = "1901",
            category = "medicine",
            prizeAmount = 150782,
            laureates = listOf(
                Laureate(
                    id = "3",
                    fullName = "Emil Adolf von Behring",
                    motivation = "for his work on serum therapy, especially its application against diphtheria",
                    birthDate = "1854-03-15",
                    birthPlace = "Hansdorf, Prussia"
                )
            )
        ),
        NobelPrize(
            year = "1901",
            category = "literature",
            prizeAmount = 150782,
            laureates = listOf(
                Laureate(
                    id = "4",
                    fullName = "Sully Prudhomme",
                    motivation = "in special recognition of his poetic composition",
                    birthDate = "1839-03-16",
                    birthPlace = "Paris, France"
                )
            )
        ),
        NobelPrize(
            year = "1901",
            category = "peace",
            prizeAmount = 150782,
            laureates = listOf(
                Laureate(
                    id = "5",
                    fullName = "Jean Henry Dunant",
                    motivation = "for his humanitarian efforts to help wounded soldiers",
                    birthDate = "1828-05-08",
                    birthPlace = "Geneva, Switzerland"
                ),
                Laureate(
                    id = "6",
                    fullName = "Frédéric Passy",
                    motivation = "for his lifelong work for international peace conferences",
                    birthDate = "1822-05-20",
                    birthPlace = "Paris, France"
                )
            )
        ),
        NobelPrize(
            year = "1903",
            category = "physics",
            prizeAmount = 141358,
            laureates = listOf(
                Laureate(
                    id = "7",
                    fullName = "Antoine Henri Becquerel",
                    motivation = "in recognition of the extraordinary services he has rendered by his discovery of spontaneous radioactivity",
                    birthDate = "1852-12-15",
                    birthPlace = "Paris, France"
                ),
                Laureate(
                    id = "8",
                    fullName = "Pierre Curie",
                    motivation = "in recognition of the extraordinary services they have rendered by their joint researches on the radiation phenomena",
                    birthDate = "1859-05-15",
                    birthPlace = "Paris, France"
                ),
                Laureate(
                    id = "9",
                    fullName = "Marie Curie, née Skłodowska",
                    motivation = "in recognition of the extraordinary services they have rendered by their joint researches on the radiation phenomena",
                    birthDate = "1867-11-07",
                    birthPlace = "Warsaw, Poland"
                )
            )
        )
    )

    fun getUser(username: String): User? = users.find { it.username == username }

    fun getAllPrizes(): List<NobelPrize> = prizes

    fun getPrizeByYearAndCategory(year: String, category: String): NobelPrize? =
        prizes.find { it.year == year && it.category == category }
}