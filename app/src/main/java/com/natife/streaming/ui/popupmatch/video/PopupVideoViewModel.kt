package com.natife.streaming.ui.popupmatch.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Video
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.data.matchprofile.MatchInfo
import com.natife.streaming.data.matchprofile.Player
import com.natife.streaming.data.player.PlayerSetup
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.MatchInfoUseCase
import com.natife.streaming.usecase.MatchProfileUseCase
import com.natife.streaming.usecase.PlayerActionUseCase
import com.natife.streaming.usecase.VideoUseCase

abstract class PopupVideoViewModel : BaseViewModel() {
    abstract val team1: LiveData<List<Player>>
    abstract val team2: LiveData<List<Player>>
    abstract val info: LiveData<MatchInfo>
    abstract val episodes: LiveData<List<Episode>>
    abstract val fullVideoDuration: LiveData<Long>
    abstract val match: LiveData<Match>
    abstract fun play(episode: Episode? = null, playList: List<Episode>? = null)
    abstract fun onStatisticClicked()
}

class PopupVideoViewModelImpl(
    private val sport: Int,
    private val matchId: Int,
    private val matchProfileUseCase: MatchProfileUseCase,
    private val matchInfoUseCase: MatchInfoUseCase,
    private val router: Router,
    private val videoUseCase: VideoUseCase,
    private val playerUseCase: PlayerActionUseCase,
) : PopupVideoViewModel() {
    private var _match: Match? = null
    override val match = MutableLiveData<Match>()
    private var videos: List<Video>? = null
    override val team1 = MutableLiveData<List<Player>>()
    override val team2 = MutableLiveData<List<Player>>()
    override val info = MutableLiveData<MatchInfo>()
    override val episodes = MutableLiveData<List<Episode>>()
    override val fullVideoDuration = MutableLiveData<Long>()
    private var matchInfo: MatchInfo? = null

    override fun play(episode: Episode?, playList: List<Episode>?) {
        router.navigate(
            PopupVideoFragmentDirections.actionGlobalPlayerFragment(
                PlayerSetup(
                    playlist =
                    mutableMapOf<String, List<Episode>>(
                        matchInfo!!.translates.ballInPlayTranslate to matchInfo!!.ballInPlay,
                        matchInfo!!.translates.highlightsTranslate to matchInfo!!.highlights,
                        matchInfo!!.translates.goalsTranslate to matchInfo!!.goals,
                        matchInfo!!.translates.fullGameTranslate to listOf(
                            Episode(
                                start = 0,
                                end = -1,
                                half = 0,
                                title = "${_match?.info}",
                                image = _match?.image ?: "",
                                placeholder = _match?.placeholder ?: ""
                            )
                        )
                    ),
                    video = videos,
                    currentEpisode = episode,
                    currentPlaylist = playList,
                    match = _match
                )
            )
        )
    }

    override fun onStatisticClicked() {
        val direction =
            PopupVideoFragmentDirections.actionPopupVideoFragmentToPopupStatisticsFragment(
                sportId = sport,
                matchId = matchId
            )
        router.navigate(direction)
    }

    init {
        launch {
            _match = matchInfoUseCase.execute(sportId = sport, matchId = matchId)
            match.value = _match
        }
        launch {
            matchInfo = matchProfileUseCase.getMatchInfo(matchId, sport)
            info.value = matchInfo
            team1.value = matchInfo?.players1
            team2.value = matchInfo?.players2
            episodes.value = matchInfo?.highlights
        }
        launch {
            val video = videoUseCase.execute(matchId, sport)
            videos = video
            fullVideoDuration.value = video.filter { it.abc == "0" }
                .groupBy { it.quality }?.entries?.maxByOrNull { it.key.toInt() }!!.value.map { (it.duration / 1000) }
                .sum()
        }
    }


}