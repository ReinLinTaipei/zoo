package com.reinlin.domain.model

sealed class Data {
    data class Exhibit(
        val id: Int,
        val name: String?,
        val info: String?,
        val category: String?,
        val picUrl: String?,
        val URL: String?
    ): Data()

    data class Plant(
        val id: Int,
        val name: String? = null,
        val nameEn: String? = null,
        val nameLatin: String? = null,
        val alsoKnown: String? = null,
        val locations: String? = null,
        val familyType: String? = null,
        val genusType: String? = null,
        val briefInfo: String? = null,
        val detail: String? = null,
        val function: String? = null,
        val picUrl: String? = null
    ): Data()

    data class NextPage(val offset: Int): Data()
}