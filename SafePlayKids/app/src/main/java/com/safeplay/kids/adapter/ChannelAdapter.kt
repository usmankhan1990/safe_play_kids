package com.safeplay.kids.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.safeplay.kids.databinding.ItemChannelBinding
import com.safeplay.kids.pojo.SearchItem

class ChannelAdapter(
    private val onClick: (channelId: String) -> Unit
) : ListAdapter<SearchItem, ChannelAdapter.ChannelViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val binding = ItemChannelBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChannelViewHolder(private val binding: ItemChannelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchItem) {
            binding.txtTitle.text = item.snippet?.title ?: "No title"
            binding.txtDescription.text = item.snippet?.description ?: "No description"

            Glide.with(binding.imgThumbnail.context)
                .load(item.snippet?.thumbnails?.default?.url)
                .into(binding.imgThumbnail)

            val channelId = item.id?.channelId
            if (!channelId.isNullOrEmpty()) {
                binding.root.setOnClickListener {
                    onClick(channelId)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem.id?.channelId == newItem.id?.channelId
            }

            override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
