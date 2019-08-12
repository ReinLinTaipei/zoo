package com.reinlin.domain.model

sealed class Zoo {
    data class Exhibits(val offset: Int, val exhibits: List<Data.Exhibit>): Zoo()
    data class Plants(val offset: Int, val plants: List<Data.Plant>): Zoo()
    data class Exception(val message: String): Zoo()
    object NoData: Zoo()
}

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

}
