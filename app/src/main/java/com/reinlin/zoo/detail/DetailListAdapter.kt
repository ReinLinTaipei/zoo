package com.reinlin.zoo.detail

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

class DetailListAdapter(
    private val dataManager: DetailListManager,
    private val listener: IZooContract.IAdapter<Data>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.item_detail -> DetailHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            else -> PlantHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_brief, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DetailHolder -> holder.bind(dataManager.data[position] as Data.Exhibit, listener::onItemClicked)
            is PlantHolder  -> holder.bind(dataManager.data[position] as Data.Plant, listener::onItemClicked)
        }
    }

    override fun getItemCount(): Int =
        dataManager.data.size

    override fun getItemViewType(position: Int): Int {
        return if (position >= 0 && position < dataManager.data.size) {
            when(dataManager.data[position]) {
                is Data.Exhibit -> R.layout.item_detail
                is Data.Plant   -> R.layout.item_brief
                else -> super.getItemViewType(position)
            }
        }
        else
            super.getItemViewType(position)
    }

    class DetailHolder(view: View): RecyclerView.ViewHolder(view) {
        private val avatar: ImageView = view.findViewById(R.id.detail_avatar)
        private val description: TextView = view.findViewById(R.id.detail_description)
        private val webBtn: TextView = view.findViewById(R.id.detail_url)

        fun bind(data: Data.Exhibit, openWeb: (Data) -> Unit) {
            data.picUrl?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_dialog_info)
                    .into(avatar)
            }

            data.info?.let { description.text = it }
            data.URL?.let { webBtn.setOnClickListener { openWeb(data) } }
        }
    }

    class PlantHolder(view: View): RecyclerView.ViewHolder(view) {

        private val avatar: ImageView = view.findViewById(R.id.brief_avatar)
        private val title: TextView = view.findViewById(R.id.brief_title)
        private val description: TextView = view.findViewById(R.id.brief_description)

        fun bind(data: Data.Plant, click: (Data) -> Unit) {
            data.picUrl?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_dialog_info)
                    .into(avatar)
            }

            data.name?.let { title.text = it }
            data.alsoKnown?.let { description.text = it }
            itemView.setOnClickListener { click(data) }
        }
    }
}