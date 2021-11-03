package com.example.websiteobserver.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.websiteobserver.R
import com.example.websiteobserver.database.entities.Website

class WebsiteListAdapter :
    ListAdapter<Website, WebsiteListAdapter.WebsiteViewHolder>(WebsiteComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebsiteViewHolder {
        return WebsiteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WebsiteViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class WebsiteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.websiteName)
        private val ping: TextView = itemView.findViewById(R.id.websitePing)
        private val url: TextView = itemView.findViewById(R.id.websiteUrl)

        private val indicator: View = itemView.findViewById(R.id.indicator)

        fun bind(entry: Website) {
            name.text = entry.name
            url.text = entry.url

            ping.text = when (entry.status) {
                "available" -> entry.lastKnownPing.toString() + " ms"
                else -> entry.status
            }

            indicator.setBackgroundColor(
                when (entry.status) {
                    "available" -> Color.parseColor("#6ee80b")
                    "not available" -> Color.parseColor("#e8110c")
                    "pending" -> Color.parseColor("#be19ab")
                    else -> Color.GRAY
                }
            )
        }

        companion object {
            fun create(parent: ViewGroup): WebsiteViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.websites_list_element, parent, false)
                return WebsiteViewHolder(view)
            }
        }
    }

    class WebsiteComparator : DiffUtil.ItemCallback<Website>() {
        override fun areItemsTheSame(oldItem: Website, newItem: Website): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Website, newItem: Website): Boolean {
            return oldItem.id == newItem.id
        }
    }

}