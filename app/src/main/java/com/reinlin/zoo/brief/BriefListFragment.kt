package com.reinlin.zoo.brief

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
import kotlinx.android.synthetic.main.fragment_brief_list.*

class BriefListFragment: Fragment(), IZooContract.BriefView, IZooContract.IAdapter<Data.Exhibit> {

    lateinit var presenter: IZooContract.ViewPresenter<BriefListManager>
    private var mainListener: IZooContract.MainView? = null
    private val event = MutableLiveData<ZooViewEvent>()
    private val dataManager: BriefListManager by lazy { presenter.getDataManager() }
    private val adapter: BriefListAdapter by lazy { brief_list.adapter as BriefListAdapter }

    companion object {
        @JvmStatic
        fun instance() : BriefListFragment = BriefListFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IZooContract.MainView) mainListener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        event.observe(this, Observer {
            presenter.observe(it)
        })
        presenter.dataFromDB.observe(this, Observer { data ->
            Log.i(TAG, "observe db(exhibits): ${data.size}")
            brief_swipe.isRefreshing = false
            data.filterIsInstance<Data.Exhibit>()
                .let {
                    dataManager.update(it, {
                        adapter.notifyItemInserted(this)
                    }, {
                        adapter.notifyItemChanged(this)
                    })
                }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_brief_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainListener?.setToolbarTitle(getString(R.string.app_name))
        brief_list.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = BriefListAdapter(dataManager, this@BriefListFragment)
        }

        brief_swipe.setOnRefreshListener {
            val offset = dataManager.getNextOffset()
            if (isAnimating().not()) event.value = ZooViewEvent.FetchExhibits(offset)
        }
        brief_swipe.isRefreshing = true
        event.value = ZooViewEvent.FetchExhibits(0)
    }

    override fun isAnimating(): Boolean =
        brief_list.isAnimating

    override fun onItemClicked(data: Data.Exhibit) {
        mainListener?.nextPage(InjectEvent.Detail(data))
    }

    override fun onFetchDone(result: Zoo) {
        brief_swipe.isRefreshing = false
        Log.i(TAG, "onFetchDone $result")
        when(result) {
            is Zoo.NoData    -> context?.toast(context!!.getString(R.string.no_data))
            is Zoo.Exception -> context?.toast(result.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.clear()
        mainListener = null
    }
}