package com.example.repository

import com.example.models.ApiResponse
import com.example.models.Hero

class HeroRepositoryImpl : HeroRepository {
    override val heroes: Map<Int, List<Hero>> by lazy {
        mapOf(
            1 to page1
        )
    }
    override val page1: List<Hero> = listOf(
        Hero(
            id = 1,
            name = "Sasuke",
            image = "",
            about = "",
            rating = 2.0,
            power = 1,
            month = "",
            day = "",
            family = listOf("", ""),
            abilities = listOf(),
            natureTypes = listOf()

        )
    )

    override val page2: List<Hero>
        get() = TODO("Not yet implemented")
    override val page3: List<Hero>
        get() = TODO("Not yet implemented")
    override val page4: List<Hero>
        get() = TODO("Not yet implemented")
    override val page5: List<Hero>
        get() = TODO("Not yet implemented")

    override suspend fun getAllHeroes(page: Int): ApiResponse {
        TODO("Not yet implemented")
    }

    override suspend fun searchHeroes(name: String): ApiResponse {
        TODO("Not yet implemented")
    }
}
