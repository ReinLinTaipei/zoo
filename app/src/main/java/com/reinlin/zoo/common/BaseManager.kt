package com.reinlin.zoo.common

import com.reinlin.domain.model.Data
import com.reinlin.zoo.model.Notify


open class BaseManager {

    protected val data: ArrayList<Data> = arrayListOf()

    protected fun <T : Data> compare(
        update: T, origin: T?,
        isDiff: (origin: T, update: T) -> Boolean
    ): Notify =
        origin?.let {
            if (isDiff(origin, update)) {
                val id = data.indexOf(origin)
                data[id] = update
                Notify.Update(id)
            } else
                Notify.Noting
        } ?: let {
            data.add(update)
            Notify.Insert(data.size - 1)
        }

    fun getData(position: Int): Data? =
        if (position < data.size) data[position] else null

    fun getOffset(): Int = data.filterIsInstance<Data.Exhibit>().last().id

    fun getCount() = data.size
}

