package com.reinlin.zoo.brief
import com.reinlin.domain.model.Data
import com.reinlin.domain.model.Notify
import com.reinlin.domain.usecase.PAGE_COUNT
import com.reinlin.zoo.common.BaseManager

class BriefListManager: BaseManager<Data>() {


    fun addData(result: Notify, notify:(startId: Int, count: Int) -> Unit) {
        val lastSize = data.size
        when(result) {
            is Notify.Forward -> {
                result.data.map { data.add(it as Data.Exhibit) }
                if (result.data.size == PAGE_COUNT) data.add(Data.NextPage)
                notify(lastSize - 1, data.size - lastSize)
            }
            is Notify.Back -> {
                result.data.map { data.add(0, it as Data.Exhibit) }
                notify(0, result.data.size)
            }
        }
    }
}


