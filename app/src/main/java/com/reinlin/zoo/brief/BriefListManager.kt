package com.reinlin.zoo.brief

import com.reinlin.domain.model.Data
import com.reinlin.domain.model.Zoo

class BriefListManager {

    val data: ArrayList<Data.Exhibit> = arrayListOf()

    fun setData(exhibits: Zoo.Exhibits) {
        data.clear()
        exhibits.exhibits.map {
            data.add(it)
        }
    }

    fun getData(position: Int): Data.Exhibit? =
        if (position < data.size) data[position] else null
}