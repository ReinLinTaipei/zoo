package com.reinlin.zoo.brief

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reinlin.domain.model.Data
import com.reinlin.zoo.IZooContract
import com.reinlin.zoo.R
import com.reinlin.zoo.common.BRIEF_INFO_LENGTH

class BriefListAdapter(
    private val dataManager: BriefListManager,
    private val listener: IZooContract.IAdapter<Data>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_brief -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_brief,
                    parent,
                    false
                )
            ) {
                listener.onItemClicked(dataManager.getData(it) as Data.Exhibit)
            }
            else -> NextHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_next_page,
                    parent,
                    false
                )
            ) {
                val offset: Int =
                    if (it >= 1)
                        (dataManager.getData(it - 1) as Data.Exhibit).id
                    else 0
                listener.onItemClicked(Data.NextPage(offset))
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (listener.isAnimating().not())
            when (getItemViewType(position)) {
                R.layout.item_brief -> (holder as ViewHolder).bind(dataManager.getData(position) as Data.Exhibit)
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

    class ViewHolder(
        view: View,
        click: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val avatar: ImageView = view.findViewById(R.id.brief_avatar)
        private val titleTextView: TextView = view.findViewById(R.id.brief_title)
        private val briefTextView: TextView = view.findViewById(R.id.brief_description)

        init {
            itemView.setOnClickListener { click(adapterPosition) }
        }

        fun bind(data: Data.Exhibit) {
            data.let {
                it.picUrl?.let { url ->
                    Glide.with(itemView)
                        .load(url)
                        .centerCrop()
                        .placeholder(android.R.drawable.ic_dialog_info)
                        .into(avatar)
                }
                it.name?.let { name -> titleTextView.text = name }
                it.info?.let { info ->
                    briefTextView.text =
                        if (info.length < BRIEF_INFO_LENGTH) info
                        else "${info.substring(0, BRIEF_INFO_LENGTH)}..."
                }
            }
        }
    }

    class NextHolder(
        view: View, click: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                click(adapterPosition)
            }
        }
    }
}