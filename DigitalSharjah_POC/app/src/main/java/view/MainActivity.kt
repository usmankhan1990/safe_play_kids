package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.digitalsharjah_poc.R
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch
import model.AppDatabase
import model.YouTubeRepository
import network.YouTubeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: VideoStatsAdapter
    private lateinit var viewModel: YouTubeViewModel
    private lateinit var database: AppDatabase
    private lateinit var apiService: YouTubeApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Database
        database = AppDatabase.getDatabase(applicationContext)

        // Initialize Retrofit for YouTube API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(YouTubeApiService::class.java)

        // Initialize Repository and ViewModel
        val repository = YouTubeRepository(apiService, database)
        viewModel = ViewModelProvider(this, YouTubeViewModelFactory(repository))[YouTubeViewModel::class.java]

        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = VideoStatsAdapter(emptyList())
        recyclerView.adapter = adapter

        // Fetch YouTube Data
        val apiKey = "AIzaSyAA8B3k-QOuKTqpDbQbdDnUWMv1otLc3dM"
        val channelId = "UCXrAgWOTHK1nUbNG3EmxniA"

        lifecycleScope.launch {
            viewModel.fetchData(apiKey, channelId)
        }

        // Observe LiveData updates from ViewModel
        viewModel.videoStats.observe(this) { stats ->
            adapter = VideoStatsAdapter(stats)
            recyclerView.adapter = adapter
        }
    }
}