package com.reinlin.zoo.detail

import android.os.Bundle
import com.reinlin.domain.model.Data
import com.reinlin.domain.model.Zoo
import com.reinlin.zoo.common.*

class DetailListManager {

    val data: ArrayList<Data> = arrayListOf()
    var exhibit: Data.Exhibit? = null 
    var fromBack = false

    fun getKeyword(): String? {
        return exhibit?.name?.splitC("(")
    }

    fun setExhibit(argument: Bundle?) {
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
    
    fun setData(plants: Zoo.Plants) {
        for (i in data.size - 1 downTo 1) {
            data.removeAt(i)
        }
        plants.plants.map {
            data.add(it)
        }
    }
}