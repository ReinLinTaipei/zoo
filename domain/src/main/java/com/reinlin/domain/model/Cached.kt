package com.reinlin.domain.model

sealed class Cached {
    data class Back(val data: Data): Cached()
    data class Forward(val data: Data): Cached()
}