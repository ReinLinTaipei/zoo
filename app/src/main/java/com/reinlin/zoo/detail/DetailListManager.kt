package com.reinlin.zoo.detail

import android.os.Bundle
import android.util.Log
import com.reinlin.domain.model.Data
import com.reinlin.zoo.common.*
import com.reinlin.zoo.model.Notify

class DetailListManager : BaseManager() {

    private var exhibit: Data.Exhibit? = null
    var fromBack = false

    fun getKeyword(): String? {
        return exhibit?.name?.splitC("(")
    }

    fun initData(argument: Bundle?) {
        data.clear()
        fromBack = false
        argument?.let {
            exhibit = Data.Exhibit(
                id = 0,
                name = it.getString(BUNDLE_NAME),
                info = it.getString(BUNDLE_INFO),
                picUrl = it.getString(BUNDLE_PIC),
                URL = it.getString(BUNDLE_URL),
                category = null
            ).also { exhibit ->
                data.add(exhibit)
            }
        }
    }

    private fun refresh(): Int {
        fromBack = false
        val lastCount = data.size
        for (i in data.size - 1 downTo 1)
            data.removeAt(i)
        return lastCount
    }

    fun update(dbData: List<Data.Plant>, notify: Notify.() -> Unit) {
        if (dbData.isEmpty()) {
            Notify.Refresh(refresh()).notify()
            return
        }

        dbData.map { update ->
            compare(update,
                data.filterIsInstance<Data.Plant>().singleOrNull { it.id == update.id },
                { old, refresh ->
                    old.name.equals(refresh.name).not() ||
                            old.detail.equals(refresh.detail).not()
                }).notify()
        }.also {
            Notify.End.notify()
        }
    }
}