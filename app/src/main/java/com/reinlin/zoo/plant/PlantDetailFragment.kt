package com.reinlin.zoo.plant

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.reinlin.domain.model.Data
import com.reinlin.zoo.IZooContract
import com.reinlin.zoo.MainActivity
import com.reinlin.zoo.R
import com.reinlin.zoo.common.*
import kotlinx.android.synthetic.main.fragment_plant_detail.*

class PlantDetailFragment: Fragment() {

    lateinit var dataManager: PlantDetailManager
    private var mainListener: IZooContract.MainView? = null

    companion object {
        @JvmStatic
        fun instance(plant: Data.Plant): PlantDetailFragment {
            val data = Bundle()
            data.putString(BUNDLE_NAME, plant.name?: "")
            data.putString(BUNDLE_LATIN, plant.nameLatin?: "")
            data.putString(BUNDLE_ALSO_KNOWN, plant.alsoKnown?: "")
            data.putString(BUNDLE_SUMMARY, plant.briefInfo?: "")
            data.putString(BUNDLE_FEATURE, plant.detail?: "")
            data.putString(BUNDLE_FUNCTIONALITY, plant.function?: "")
            data.putString(BUNDLE_PIC, plant.picUrl?: "")
            return PlantDetailFragment().apply {
                arguments = data
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IZooContract.MainView) mainListener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataManager.setData(arguments)
        mainListener?.setToolbarTitle(dataManager.plant?.name)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_plant_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataManager.plant?.let {
            (plant_name as TextView).text = it.name?: ""
            (plant_latin_name as TextView).text = it.nameLatin?: ""
            (plant_also_known_description as TextView).text = it.alsoKnown?: ""
            (plant_summary_description as TextView).text = it.briefInfo?: ""
            (plant_feature_description as TextView).text = it.detail?: ""
            (plant_functionality_description as TextView).text = it.function?: ""
            if (TextUtils.isEmpty(it.picUrl).not()) {
                Glide.with(context!!)
                    .load(it.picUrl)
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_dialog_info)
                    .into(plant_avatar)
            }
        }
    }

}