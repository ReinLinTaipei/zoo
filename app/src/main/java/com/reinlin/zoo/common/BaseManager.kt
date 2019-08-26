package com.reinlin.zoo.common

import com.reinlin.domain.model.Data


open class BaseManager {

    protected val data: ArrayList<Data> = arrayListOf()

    protected fun <T : Data> compare(
        update: T, origin: T?,
        isDiff: (origin: T, update: T) -> Boolean
    ): Compare =
        origin?.let {
            if (isDiff(origin, update)) {
                val id = data.indexOf(origin)
                data[id] = update
                Compare.Update(id)
            } else
                Compare.Noting
        } ?: let {
            data.add(update)
            Compare.Insert(data.size - 1)
        }

    fun getData(position: Int): Data? =
        if (position < data.size) data[position] else null

    fun getOffset(): Int = data.filterIsInstance<Data.Exhibit>().last().id

    fun getCount() = data.size
}

sealed class Compare {
    data class Insert(val position: Int) : Compare()
    data class Update(val position: Int) : Compare()
    object Noting : Compare()
}