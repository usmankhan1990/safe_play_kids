package com.safeplay.kids.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.safeplay.kids.R

class VideoPlayerFragment : Fragment() {

    companion object {
        private const val ARG_VIDEO_ID = "videoId"

        fun newInstance(videoId: String) = VideoPlayerFragment().apply {
            arguments = Bundle().apply { putString(ARG_VIDEO_ID, videoId) }
        }
    }

    private var playerView: YouTubePlayerView? = null
    private var windowController: WindowInsetsControllerCompat? = null

    // ──────────────────────────────────────────────────────────────────────────
    //  LIFECYCLE
    // ──────────────────────────────────────────────────────────────────────────
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_video_player, container, false).also { root ->
            playerView = root.findViewById(R.id.youtube_player_view)

            // Tuck system-bars for immersive experience
            requireActivity().window?.let { w ->
                windowController = WindowInsetsControllerCompat(w, w.decorView)
                windowController?.hide(WindowInsetsCompat.Type.systemBars())
                windowController?.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val videoId = arguments?.getString(ARG_VIDEO_ID) ?: return
        val ytp = playerView ?: return

        lifecycle.addObserver(ytp)

        ytp.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                player.loadVideo(videoId, /* startSeconds = */ 0f)   // autoplay
            }
        })
    }

    override fun onDestroyView() {
        playerView?.let {
            lifecycle.removeObserver(it)
            it.release()
        }
        playerView = null

        // Restore system-bars
        windowController?.show(WindowInsetsCompat.Type.systemBars())
        windowController = null

        super.onDestroyView()
    }
}