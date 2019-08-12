package com.reinlin.zoo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.reinlin.zoo.brief.BriefListFragment
import com.reinlin.zoo.common.BRIEF_LIST
import com.reinlin.zoo.common.attachFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        BriefListFragment.instance().let { briefView ->
            ZooInjector().buildBriefPresenter(briefView)
            attachFragment(supportFragmentManager, briefView, BRIEF_LIST)
        }
    }
}
