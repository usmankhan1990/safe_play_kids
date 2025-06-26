package view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digitalsharjah_poc.R
import model.VideoStatsEntity

class VideoStatsAdapter(private val stats: List<VideoStatsEntity>) : RecyclerView.Adapter<VideoStatsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.videoTitle)
        val views: TextView = itemView.findViewById(R.id.videoViews)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = stats[position]
        holder.title.text = video.title
        holder.views.text = "Views: ${video.views}+"
    }

    override fun getItemCount(): Int = stats.size
}
