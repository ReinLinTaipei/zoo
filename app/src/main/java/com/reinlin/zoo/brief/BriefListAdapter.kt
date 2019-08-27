package com.reinlin.zoo.brief

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reinlin.domain.model.Data
import com.reinlin.zoo.IZooContract
import com.reinlin.zoo.R
import com.reinlin.zoo.common.BRIEF_INFO_LENGTH
import com.reinlin.zoo.common.TAG

class BriefListAdapter(
    private val dataManager: BriefListManager,
    private val listener: IZooContract.IAdapter
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_brief -> BriefHolder(parent) {
                listener.onItemClicked(dataManager.getData(it) as Data.Exhibit)
            }
            else -> NextHolder(parent) {
                listener.onItemClicked(dataManager.getData(it))
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (listener.isAnimating().not())
            when(position) {
                in 0 until itemCount -> when (getItemViewType(position)) {
                    R.layout.item_brief -> (holder as BriefHolder).bind(dataManager.getData(position) as Data.Exhibit)
                }
            }
    }

    override fun getItemCount(): Int =
        dataManager.getCount()

    override fun getItemViewType(position: Int): Int {
        return when (dataManager.getData(position)) {
            is Data.Exhibit -> R.layout.item_brief
            is Data.NextPage -> R.layout.item_next_page
            else -> super.getItemViewType(position)
        }
    }

    fun refresh() {
        dataManager.apply {
            val count = getCount()
            if (count > 0) {
                clear()
                notifyItemRangeRemoved(0, count)
                addData(Data.NextPage(0))
                notifyItemInserted(getCount())
            }
        }
    }

    class BriefHolder(
        parent: ViewGroup,
        click: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_brief,
        parent,
        false
    )) {

        private val avatar: ImageView       by lazy { itemView.findViewById<ImageView>(R.id.brief_avatar) }
        private val titleTextView: TextView by lazy { itemView.findViewById<TextView>(R.id.brief_title) }
        private val briefTextView: TextView by lazy { itemView.findViewById<TextView>(R.id.brief_description) }

        init {
            itemView.setOnClickListener { click(adapterPosition) }
        }

        fun bind(data: Data.Exhibit) {
            Log.i(TAG, "bind ($adapterPosition)(id:${data.id})${data.name}")
            data.let {
                it.picUrl?.let { url ->
                    Glide.with(itemView)
                        .load(url)
                        .centerCrop()
                        .placeholder(android.R.drawable.ic_dialog_info)
                        .into(avatar)
                }
                it.name?.let { name -> titleTextView.text = itemView.context.getString(R.string.brief_title, it.id.toString(), name) }
                it.info?.let { info ->
                    briefTextView.text =
                        if (info.length < BRIEF_INFO_LENGTH) info
                        else "${info.substring(0, BRIEF_INFO_LENGTH)}..."
                }
            }
        }
    }

    class NextHolder(
        parent: ViewGroup, val click: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_next_page,
        parent,
        false
    )) {
        init {
            itemView.setOnClickListener { click(adapterPosition) }
        }
    }
}