package com.reinlin.zoo.common


open class BaseManager<T> {

    protected val data: ArrayList<T> = arrayListOf()

    protected fun compareItem(index: Int, update: T, origin: T?,
                            isDiff: (origin: T, update: T) -> Boolean,
                            insert:(Int) -> Unit, notify: (Int) -> Unit) {
        origin?.let {
            if (isDiff(origin, update)) {
                data[index] = update
                notify(index)
            }
        } ?: let {
            data.add(update)
            insert(data.size-1)
        }
    }

    fun getData(position: Int): T? =
        if (position < data.size) data[position] else null

    fun getNextOffset(): Int = data.size

    fun getCount() = data.size
}