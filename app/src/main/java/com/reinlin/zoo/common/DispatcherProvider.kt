package com.reinlin.zoo.common

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object DispatcherProvider {

    val contextUI: CoroutineContext
        get() = Dispatchers.Main

    val contextIO: CoroutineContext
            get() = Dispatchers.IO
}