package view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import model.YouTubeRepository

class YouTubeViewModelFactory(private val repository: YouTubeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(YouTubeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return YouTubeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
