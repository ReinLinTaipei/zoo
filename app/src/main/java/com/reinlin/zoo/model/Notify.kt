package com.reinlin.zoo.model

sealed class Notify {
    data class Insert(val position: Int) : Notify()
    data class Update(val position: Int) : Notify()
    data class Remove(val position: Int) : Notify()
    data class Refresh(val lastCount: Int): Notify()
    object Noting : Notify()
    object End: Notify()
}