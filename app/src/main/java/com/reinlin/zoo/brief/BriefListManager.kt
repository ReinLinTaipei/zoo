package com.reinlin.zoo.brief

import android.util.Log
import com.reinlin.domain.model.Data
import com.reinlin.zoo.common.BaseManager
import com.reinlin.zoo.common.TAG
import com.reinlin.zoo.model.Notify

class BriefListManager : BaseManager() {

    fun update(dbData: List<Data.Exhibit>, notify: Notify.() -> Unit) {

        Log.i(TAG, "update exhibits from DB: ${dbData.size}")

        removeNextItem(notify)

        dbData.map { update ->
            data.filterIsInstance<Data.Exhibit>()
                .singleOrNull { it.id == update.id }
                .let { old ->
                    compare(update, old) { ori, upd ->
                        ori.name.equals(upd.name).not() ||
                                ori.info.equals(upd.info)
                    }.notify()
                }
        }.also {
            if (data.isNotEmpty()) {
                val offset = (data.last() as Data.Exhibit).id
                data.add(Data.NextPage(offset + 1))
                Notify.Insert(data.size - 1).notify()
            }
            Notify.End.notify()
        }
    }

    private fun removeNextItem(notify: Notify.() -> Unit) {
        data.singleOrNull { it is Data.NextPage }
            ?.let {
                val id = data.indexOf(it)
                data.remove(it)
                Notify.Remove(id).notify()
            }
    }

    fun refresh(notify: Notify.Refresh.() -> Unit) {
        val lastCount = getCount()
        clear()
        Notify.Refresh(lastCount).notify()
    }
}


