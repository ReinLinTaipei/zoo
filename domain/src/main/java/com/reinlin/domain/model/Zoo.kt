package com.reinlin.domain.model

sealed class Zoo {
    data class Exhibits(val offset: Int, val exhibits: List<Data.Exhibit>): Zoo()
    data class Plants(val offset: Int, val plants: List<Data.Plant>): Zoo()
    data class Exception(val message: String): Zoo()
    object NoData: Zoo()
}

