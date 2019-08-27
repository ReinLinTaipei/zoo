package com.reinlin.zoo.common

import com.reinlin.domain.model.Data
import com.reinlin.zoo.model.Notify


open class BaseManager {

    protected val data: ArrayList<Data> = arrayListOf()

    protected fun <T : Data> compare(
        update: T, old: T?,
        isDiff: (old: T, update: T) -> Boolean
    ): Notify =
        old?.let {
            if (isDiff(old, update)) {
                val id = data.indexOf(old)
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

    fun getCount() = data.size

    fun clear() = data.clear()

    fun addData(item: Data) {
        data.add(item)
    }
}

