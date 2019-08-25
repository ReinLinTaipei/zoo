package com.reinlin.domain.model

sealed class Notify {
    data class Back(val data: List<Data>): Notify()
    data class Forward(val data: List<Data>): Notify()
    data class Result(val result: Zoo): Notify()
}