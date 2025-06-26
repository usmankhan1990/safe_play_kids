package com.safeplay.kids.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.safeplay.kids.R
import com.safeplay.kids.pojo.SearchSuggestionItem

// 2. VideoAdapter.kt
class VideoAdapter(
    private val onClick: (SearchSuggestionItem.VideoItem) -> Unit
) : ListAdapter<SearchSuggestionItem.VideoItem, VideoAdapter.VideoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SearchSuggestionItem.VideoItem) {
            val title = itemView.findViewById<TextView>(R.id.txtTitle)
            val thumb = itemView.findViewById<ImageView>(R.id.imgThumb)

            title.text = item.title
            Glide.with(itemView)
                .load(item.thumbnailUrl)
                .into(thumb)

            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SearchSuggestionItem.VideoItem>() {
        override fun areItemsTheSame(
            old: SearchSuggestionItem.VideoItem,
            new: SearchSuggestionItem.VideoItem
        ): Boolean = old.videoId == new.videoId

        override fun areContentsTheSame(
            old: SearchSuggestionItem.VideoItem,
            new: SearchSuggestionItem.VideoItem
        ): Boolean = old == new
    }
}