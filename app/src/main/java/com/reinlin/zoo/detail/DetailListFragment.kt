package com.reinlin.zoo.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.reinlin.zoo.model.Notify
import kotlinx.android.synthetic.main.fragment_detail_list.*

class DetailListFragment: Fragment(), IZooContract.DetailView, IZooContract.IAdapter {

    lateinit var presenter: IZooContract.ViewPresenter<DetailListManager>
    private var mainListener: IZooContract.MainView? = null
    private val event = MutableLiveData<ZooViewEvent>()
    private val dataManager: DetailListManager by lazy { presenter.getDataManager() }
    private val adapter: DetailListAdapter? by lazy { detail_list.adapter as? DetailListAdapter }

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IZooContract.MainView) mainListener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.getDataManager().initData(arguments)
        event.observe(this, Observer {
            presenter.observe(it)
        })
        presenter.dataFromDB.observe(this, Observer {
            detail_swipe.isRefreshing = false
            dataManager.update(it.map { db -> db as Data.Plant }) {
                when(this) {
                    is Notify.Insert  -> adapter?.notifyItemInserted(this.position)
                    is Notify.Update  -> adapter?.notifyItemInserted(this.position)
                    is Notify.Refresh -> adapter?.notifyItemRangeRemoved(1, lastCount-1)
                        .also { if (isAnimating().not()) event.value = ZooViewEvent.FetchPlants(dataManager.getKeyword()) }
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainListener?.setToolbarTitle(dataManager.exhibit?.name)

        detail_list.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = DetailListAdapter(dataManager, this@DetailListFragment)
        }

        detail_swipe.setOnRefreshListener {
            if (isAnimating().not()) event.value = ZooViewEvent.DeletePlants
        }

        if (dataManager.fromBack.not()) {
            detail_swipe.isRefreshing = true
            event.value = ZooViewEvent.FetchPlants(dataManager.getKeyword())
        }
    }

    override fun onFetchDone(result: Zoo) {
        Log.i(TAG, "on fetch done: $result")
        detail_swipe.isRefreshing = false
        when(result) {
            is Zoo.NoData -> {}
            is Zoo.Exception  -> { context?.toast(result.message)}
        }
    }

    override fun<T> onItemClicked(data: T) {
        when(data) {
            is Data.Plant -> {
                activity?.let {
                    presenter.getDataManager().fromBack = true
                    mainListener?.nextPage(InjectEvent.Plant(data))
                }
            }
        }
    }

    override fun isAnimating(): Boolean =
        detail_list.isAnimating

    override fun onDestroy() {
        super.onDestroy()
        presenter.clear()
        mainListener = null
    }
}