package com.reinlin.zoo.brief
import com.reinlin.domain.model.Data
import com.reinlin.zoo.common.BaseManager

class BriefListManager: BaseManager<Data.Exhibit>() {

    fun update(updates: List<Data.Exhibit>, insert: Int.() -> Unit, notify: Int.() -> Unit): Int {
        updates.forEachIndexed { index, update ->
            compareItem(index, update, data.singleOrNull { it.id == update.id }, { compare, target ->
                if (target.name.equals(compare.name).not()) return@compareItem true
                if (target.info.equals(compare.info).not()) return@compareItem true
                return@compareItem false
            }, insert, notify)
        }
        return getCount()
    }
}


