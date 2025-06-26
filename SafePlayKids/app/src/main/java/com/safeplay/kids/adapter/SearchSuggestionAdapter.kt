package com.safeplay.kids.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ArrayAdapter
import com.safeplay.kids.R
import com.safeplay.kids.pojo.SearchItem
import com.safeplay.kids.pojo.SearchSuggestionItem

class SearchSuggestionAdapter(
    context: Context,
    private val onClick: (SearchSuggestionItem) -> Unit
) : ArrayAdapter<SearchSuggestionItem>(context, R.layout.item_channel_suggestion) {

    private val items = mutableListOf<SearchSuggestionItem>()

    fun setItems(newItems: List<SearchSuggestionItem>) {
        items.clear()
        items.addAll(newItems)
        clear()
        addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): SearchSuggestionItem? = items[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_channel_suggestion, parent, false)

        val item = getItem(position)
        val txt = view.findViewById<TextView>(android.R.id.text1)

        txt.text = when (item) {
            is SearchSuggestionItem.ChannelItem -> "🔵 ${item.title}"  // Optional: prefix for visual hint
            is SearchSuggestionItem.VideoItem   -> "▶️ ${item.title}"
            else -> ""
        }

        txt.setOnClickListener { item?.let { onClick(it) } }

        return view
    }
}