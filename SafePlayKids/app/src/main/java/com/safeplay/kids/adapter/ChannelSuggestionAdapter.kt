package com.safeplay.kids.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.safeplay.kids.databinding.ItemChannelSuggestionBinding
import com.safeplay.kids.pojo.SearchItem

class ChannelSuggestionAdapter(
    ctx: Context,
    private val onClick: (SearchItem) -> Unit
) : ArrayAdapter<SearchItem>(ctx, 0) {

    private val items = mutableListOf<SearchItem>()

    fun setItems(newItems: List<SearchItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): SearchItem? = items[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            ItemChannelSuggestionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        } else ItemChannelSuggestionBinding.bind(convertView)

        val item = items[position]
        binding.txtTitle.text = item.snippet?.title
        binding.txtDesc.text  = item.snippet?.description
        Glide.with(binding.imgThumb)
            .load(item.snippet?.thumbnails?.default?.url)
            .into(binding.imgThumb)

        binding.root.setOnClickListener { onClick(item) }
        return binding.root
    }
}
