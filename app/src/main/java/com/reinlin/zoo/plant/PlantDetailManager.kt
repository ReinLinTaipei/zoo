package com.reinlin.zoo.plant

import android.os.Bundle
import com.reinlin.domain.model.Data
import com.reinlin.zoo.common.*

class PlantDetailManager {

    var plant: Data.Plant? = null

    fun setData(arguments: Bundle?) {
        arguments?.let {
            plant = Data.Plant(
                id = 0,
                name = arguments.getString(BUNDLE_NAME),
                nameLatin = arguments.getString(BUNDLE_LATIN),
                alsoKnown = arguments.getString(BUNDLE_ALSO_KNOWN),
                briefInfo = arguments.getString(BUNDLE_SUMMARY),
                detail = arguments.getString(BUNDLE_FEATURE),
                function = arguments.getString(BUNDLE_FUNCTIONALITY),
                picUrl = arguments.getString(BUNDLE_PIC)
            )
        }
    }
}