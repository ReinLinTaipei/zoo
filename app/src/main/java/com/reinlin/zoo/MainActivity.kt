package com.reinlin.zoo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.reinlin.zoo.brief.BriefListFragment
import com.reinlin.zoo.common.*
import com.reinlin.zoo.common.attachFragment
import com.reinlin.zoo.detail.DetailListFragment
import com.reinlin.zoo.plant.PlantDetailFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IZooContract.MainView {

    private val zooInjector: ZooInjector by lazy { ZooInjector() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        attachFragment(zooInjector.buildBriefPresenter(BriefListFragment.instance()), BRIEF_LIST)
    }

    override fun setToolbarTitle(title: String?) =
        toolbarTitle(title)

    override fun nextPage(event: InjectEvent) {
        when(event) {
            is InjectEvent.Detail ->
                attachFragment(zooInjector.buildDetailPresenter(DetailListFragment.instance(event.data)), DETAIL_EXHIBIT)
            is InjectEvent.Plant  ->
                attachFragment(zooInjector.buildPlantManager(PlantDetailFragment.instance(event.data)), DETAIL_PLANT)
        }
    }
}
