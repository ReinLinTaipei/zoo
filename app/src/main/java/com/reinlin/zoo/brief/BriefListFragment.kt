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
import com.reinlin.zoo.model.Notify
import kotlinx.android.synthetic.main.fragment_brief_list.*

class BriefListFragment: Fragment(), IZooContract.PageView {

    lateinit var presenter: IZooContract.ViewPresenter<BriefListManager>
    private var mainListener: IZooContract.MainView? = null
    private val event = MutableLiveData<ZooViewEvent>()
    private val dataManager: BriefListManager by lazy { presenter.getDataManager() }
    private val adapter: BriefListAdapter by lazy { brief_list.adapter  as BriefListAdapter }

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
            Log.i(TAG, "update db (${data.size})$data")
            if (data.isEmpty())
                dataManager.refresh { adapter.notifyItemRangeRemoved(0, this.lastCount).also { fetchData(0) } }
            else
                dataManager.update(data.map { it as Data.Exhibit }) {
                    when(this) {
                        is Notify.Update  -> adapter.notifyItemChanged(position)
                        is Notify.Insert  -> adapter.notifyItemInserted(position)
                        is Notify.Remove  -> adapter.notifyItemRemoved(position)
                        is Notify.End     -> scrollToPosition()
                    }
                }
        })
    }

    private fun scrollToPosition() {
        brief_swipe.isRefreshing = false
        if (isAnimating().not())
            brief_list.layoutManager?.scrollToPosition(adapter.itemCount-1)
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
            Log.i(TAG, "delete all")
            event.value = ZooViewEvent.RefreshExhibit
        }
    }


    override fun isAnimating(): Boolean =
        brief_list.isAnimating


    override fun <T> onItemClicked(data: T) {
        when(data) {
            is Data.Exhibit  -> mainListener?.nextPage(InjectEvent.Detail(data))
            is Data.NextPage -> fetchData(data.offset)
        }
    }

    private fun fetchData(offset: Int) {
        Log.i(TAG, "fetchData: $offset")
        brief_swipe.isRefreshing = true
        event.value = ZooViewEvent.FetchExhibits(offset)
    }

    override fun notify(data: Zoo) {
        Log.i(TAG, "notify $data")
        brief_swipe.isRefreshing = false
        when(data) {
            is Zoo.NoData    -> context?.toast(context!!.getString(R.string.no_data))
            is Zoo.Exception -> context?.toast(data.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.clear()
        mainListener = null
    }
}