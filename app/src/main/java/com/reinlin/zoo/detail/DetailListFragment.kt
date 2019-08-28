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
import com.reinlin.zoo.IZooContract
import com.reinlin.zoo.InjectEvent
import com.reinlin.zoo.R
import com.reinlin.zoo.ZooViewEvent
import com.reinlin.zoo.common.*
import com.reinlin.zoo.model.Notify
import kotlinx.android.synthetic.main.fragment_detail_list.*

class DetailListFragment : Fragment(), IZooContract.PageView {

    lateinit var presenter: IZooContract.ViewPresenter<DetailListManager>
    private var mainListener: IZooContract.MainView? = null
    private val event = MutableLiveData<ZooViewEvent>()
    private val dataManager: DetailListManager by lazy { presenter.getDataManager() }
    private val adapter: DetailListAdapter? by lazy { detail_list.adapter as? DetailListAdapter }

    companion object {
        @JvmStatic
        fun instance(exhibit: Data.Exhibit): DetailListFragment {
            val data = Bundle()
            data.putString(BUNDLE_NAME, exhibit.name ?: "")
            data.putString(BUNDLE_INFO, exhibit.info ?: "")
            data.putString(BUNDLE_PIC, exhibit.picUrl ?: "")
            data.putString(BUNDLE_URL, exhibit.URL ?: "")
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

            dataManager.update(it.map { db -> db as Data.Plant }) {
                when (this) {
                    is Notify.Insert -> adapter?.notifyItemInserted(this.position)
                    is Notify.Update -> adapter?.notifyItemInserted(this.position)
                    is Notify.Refresh -> adapter?.notifyItemRangeRemoved(1, lastCount - 1)
                        .also { fetchPlants() }
                    is Notify.End -> detail_swipe.isRefreshing = false
                }
            }
        })
    }

    private fun fetchPlants() {
        if (isAnimating().not()) {
            detail_swipe.isRefreshing = true
            event.value = ZooViewEvent.FetchPlants(dataManager.getKeyword())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainListener?.setToolbarTitle(dataManager.getKeyword())

        detail_list.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = DetailListAdapter(dataManager, this@DetailListFragment)
        }

        detail_swipe.setOnRefreshListener {
            refresh()
        }

        refresh()
    }

    private fun refresh() {
        if (isAnimating().not()) event.value = ZooViewEvent.RefreshPlants
    }

    override fun notify(data: Zoo) {
        detail_swipe.isRefreshing = false
        when (data) {
            is Zoo.NoData -> context?.toast(getString(R.string.no_data))
            is Zoo.Exception -> {
                context?.toast(data.message)
            }
        }
    }

    override fun <T> onItemClicked(data: T) {
        when (data) {
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