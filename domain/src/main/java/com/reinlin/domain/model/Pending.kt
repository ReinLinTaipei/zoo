package com.reinlin.domain.model

sealed class Pending {
    data class Back(val position: Int, val offset: Int) : Pending()
    data class Forward(val position: Int, val offset: Int) : Pending()
    data class Cached(val data: List<Data>): Pending()
}