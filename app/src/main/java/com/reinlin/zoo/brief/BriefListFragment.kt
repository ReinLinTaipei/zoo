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
import com.reinlin.domain.model.Notify
import com.reinlin.domain.model.Zoo
import com.reinlin.zoo.*
import com.reinlin.zoo.common.*
import com.reinlin.zoo.common.toast
import kotlinx.android.synthetic.main.fragment_brief_list.*

class BriefListFragment: Fragment(), IZooContract.BriefView, IZooContract.IAdapter<Data> {

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
//        presenter.dataFromDB.observe(this, Observer { data ->
//            Log.i(TAG, "observe exhibits from DB: ${data.size}")
//            brief_swipe.isRefreshing = false
//            data.filterIsInstance<Data.Exhibit>()
//                .let {
//                    dataManager.update(it, {
//                        adapter.notifyItemInserted(this)
//                    }, {
//                        adapter.notifyItemChanged(this)
//                    })
//                }
//        })
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
            val offset = dataManager.getOffset()
            if (isAnimating().not()) event.value = ZooViewEvent.FetchExhibits(offset)
        }
        nextPage(0)
    }

    private fun nextPage(id: Int) {
        brief_swipe.isRefreshing = true
        event.value = ZooViewEvent.FetchExhibits(id)
    }

    override fun isAnimating(): Boolean =
        brief_list.isAnimating

    override fun onItemClicked(data: Data) {
        when(data) {
            is Data.Exhibit  -> mainListener?.nextPage(InjectEvent.Detail(data))
            is Data.NextPage -> nextPage(data.position)
        }
    }

    override fun notify(result: Notify) {
        Log.i(TAG, "onFetchDone $result")
        brief_swipe.isRefreshing = false
        when(result) {
            is Notify.Result  -> {
                result.result.apply {
                    when(this) {
                        is Zoo.NoData    -> context?.toast(context!!.getString(R.string.no_data))
                        is Zoo.Exception -> context?.toast(this.message)
                    }
                }
            }
            else -> dataManager.addData(result, adapter::notifyItemRangeInserted)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.clear()
        mainListener = null
    }
}