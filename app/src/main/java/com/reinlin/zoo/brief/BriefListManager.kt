package com.reinlin.zoo.brief
import com.reinlin.domain.model.Data

class BriefListManager {

    private val data: ArrayList<Data.Exhibit> = arrayListOf()

    fun update(updates: List<Data.Exhibit>, insert: Int.() -> Unit, notify: Int.() -> Unit): Int {
        updates.forEachIndexed { index, update ->
            updateItem(index, update, data.singleOrNull { it.id == update.id }, { compare, target ->
                if (target.name.equals(compare.name).not()) return@updateItem true
                if (target.info.equals(compare.info).not()) return@updateItem true
                return@updateItem false
            }, insert, notify)
        }
        return getCount()
    }

    private fun updateItem(index: Int, update: Data.Exhibit, origin: Data.Exhibit?,
                           isDiff: (origin: Data.Exhibit, update: Data.Exhibit) -> Boolean,
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

    fun getData(position: Int): Data.Exhibit? =
        if (position < data.size) data[position] else null

    fun getNextOffset(): Int = data.size

    fun getCount() = data.size
}


