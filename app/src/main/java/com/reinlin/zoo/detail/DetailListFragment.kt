package com.reinlin.zoo.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reinlin.domain.model.Data
import com.reinlin.domain.model.Zoo
import com.reinlin.zoo.*
import com.reinlin.zoo.common.*
import com.reinlin.zoo.common.toast
import com.reinlin.zoo.plant.PlantDetailFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail_list.*

class DetailListFragment: Fragment(), IZooContract.DetailView, IZooContract.IAdapter<Data> {

    lateinit var presenter: IZooContract.DetailPresenter
    private val event = MutableLiveData<ZooViewEvent>()

    companion object {
        @JvmStatic
        fun instance(exhibit: Data.Exhibit): DetailListFragment {
            val data = Bundle()
            data.putString(BUNDLE_NAME, exhibit.name?: "")
            data.putString(BUNDLE_INFO, exhibit.info?: "")
            data.putString(BUNDLE_PIC, exhibit.picUrl?: "")
            data.putString(BUNDLE_URL, exhibit.URL?: "")
            return DetailListFragment().apply {
                arguments = data
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.getDataManager().setExhibit(arguments)
        event.observe(this, Observer {
            presenter.observe(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).toolbarTitle(presenter.getDataManager().exhibit?.name)
        detail_list.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = DetailListAdapter(presenter.getDataManager(), this@DetailListFragment)
        }

        detail_swipe.setOnRefreshListener {
            event.value = ZooViewEvent.FetchPlants(presenter.getDataManager().exhibit?.name)
        }

        if (presenter.getDataManager().fromBack.not()) {
            detail_swipe.isRefreshing = true
            event.value = ZooViewEvent.FetchPlants(presenter.getDataManager().getKeyword())
        }
    }

    override fun onFetchDone(result: Zoo) {
        detail_swipe.isRefreshing = false
        when(result) {
            is Zoo.Plants -> {
                presenter.getDataManager().setData(result)
                (detail_list.adapter as DetailListAdapter).notifyItemRangeInserted(1, presenter.getDataManager().data.size - 1)
            }
            is Zoo.NoData -> {}
            is Zoo.Exception -> { context?.toast(result.message)}
        }
    }

    override fun onItemClicked(data: Data) {
        when(data) {
            is Data.Exhibit -> {

            }
            is Data.Plant -> {
                activity?.let {
                    presenter.getDataManager().fromBack = true
                    PlantDetailFragment.instance(data).apply {
                        ZooInjector().buildPlantManager(this)
                        it.attachFragment(it.supportFragmentManager,
                            this,
                            DETAIL_PLANT)
                    }
                }
            }
        }
    }

    override fun isAnimating(): Boolean =
        detail_list.isAnimating

    override fun onDestroy() {
        super.onDestroy()
        presenter.clear()
    }
}