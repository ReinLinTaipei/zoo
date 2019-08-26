package com.reinlin.zoo.brief

import com.reinlin.domain.model.Data
import com.reinlin.domain.usecase.PAGE_COUNT
import com.reinlin.zoo.common.BaseManager
import com.reinlin.zoo.common.Compare

class BriefListManager : BaseManager() {

    fun update(updates: List<Data.Exhibit>, notify: Compare.() -> Unit) {
        updates.map { update ->
            data.filterIsInstance<Data.Exhibit>()
                .singleOrNull { it.id == update.id }
                .let { origin ->
                    compare(update, origin) { ori, upd ->
                        ori.name.equals(upd.name).not() ||
                                ori.info.equals(upd.info)
                    }.notify()
                }
        }
    }

    fun addData(result: List<Data.Exhibit>, notify: (startId: Int, count: Int) -> Unit) {
        val lastSize = data.size
        if (result.size == PAGE_COUNT)
            data.add(Data.NextPage(data.size))
        notify(lastSize - 1, data.size - lastSize)
    }
}


