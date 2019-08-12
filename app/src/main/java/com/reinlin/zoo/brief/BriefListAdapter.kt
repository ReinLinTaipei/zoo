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
    val dataManager: BriefListManager,
    private val listener: IZooContract.IAdapter<Data.Exhibit>): RecyclerView.Adapter<BriefListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_brief, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (listener.isAnimating().not()) holder.bind(dataManager.getData(position), listener::onItemClicked)
    }

    override fun getItemCount(): Int =
        dataManager.data.size

    class ViewHolder(view: View,
                     private val avatar: ImageView = view.findViewById(R.id.brief_avatar),
                     private val titleTextView: TextView = view.findViewById(R.id.brief_title),
                     private val briefTextView: TextView = view.findViewById(R.id.brief_description)): RecyclerView.ViewHolder(view) {


        fun bind(data: Data.Exhibit?, click:(Data.Exhibit) -> Unit) {
            data?.let {
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
                itemView.setOnClickListener { click(data) }
            }
        }
    }
}