package com.reinlin.zoo.common

import kotlinx.coroutines.Job

abstract class BasePresenter(val dispatcher: DispatcherProvider) {
    lateinit var job: Job
}