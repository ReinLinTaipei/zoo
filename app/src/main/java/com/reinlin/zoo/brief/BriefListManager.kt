package com.reinlin.zoo.brief
import com.reinlin.domain.model.Data

class BriefListManager {

    private val data: ArrayList<Data.Exhibit> = arrayListOf()

    fun setData(exhibits: List<Data.Exhibit>): Int {
        data.addAll(exhibits)
        return getCount()
    }


    fun getData(position: Int): Data.Exhibit? =
        if (position < data.size) data[position] else null

    fun clearData(): Int {
        val lastCount = data.size
        data.clear()
        return lastCount
    }

    fun getNextOffset(): Int = if (data.size > 0) data.size else 0

    fun getCount() = data.size
}


