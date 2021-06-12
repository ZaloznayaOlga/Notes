package com.natife.streaming.ui.popupmatch.video


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.bindTeamImage
import com.natife.streaming.ext.predominantColorToGradient
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.subscribeEvent
import com.natife.streaming.ui.main.MainActivity
import com.natife.streaming.ui.popupmatch.PopupSharedViewModel
import com.natife.streaming.ui.popupmatch.video.additionaly.TabAdditionallyFragment
import com.natife.streaming.ui.popupmatch.video.byplayers.TabByPlayersFragment
import com.natife.streaming.ui.popupmatch.video.langues.TabLanguagesFragment
import com.natife.streaming.ui.popupmatch.video.matchivents.TabMatchEventsFragment
import com.natife.streaming.ui.popupmatch.video.watch.TabWatchFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_popup_video.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf


class PopupVideoFragment : BaseFragment<PopupVideoViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_popup_video
    private lateinit var popupVideoNames: Array<String>
    private val popupSharedViewModel: PopupSharedViewModel by navGraphViewModels(R.id.popupVideo)

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("matchId")?.let {
            popupSharedViewModel.matchId = it
        }
        arguments?.getInt("sportId")?.let {
            popupSharedViewModel.sportId = it
        }
        (activity as MainActivity).main_group?.visibility = View.GONE
        (activity as MainActivity).popup_group?.visibility = View.VISIBLE
        //Heading in the predominant team color
        (activity as MainActivity).mainMotion?.predominantColorToGradient("#CCCB312A")

        (activity as MainActivity).statistics_button?.apply {
            setOnClickListener {
                viewModel.onStatisticClicked()
            }
            text = resources.getString(R.string.statistics)
        }

        subscribe(viewModel.match) {
            popupSharedViewModel.setMatch(it)
            (activity as MainActivity).logo_first_team?.bindTeamImage(it.sportId, it.team1.id)
            (activity as MainActivity).name_first_team?.text = it.team1.name.take(3)
            (activity as MainActivity).logo_second_team?.bindTeamImage(it.sportId, it.team2.id)
            (activity as MainActivity).name_second_team?.text = it.team2.name.take(3)
            (activity as MainActivity).score_text?.text = "${it.team1.score} - ${it.team2.score}"
        }
        subscribe(viewModel.info) {
            popupSharedViewModel.setMatchInfo(it)
        }
        subscribe(viewModel.fullVideoDuration) {
            popupSharedViewModel.setFullVideoDuration(it)
        }
        subscribe(viewModel.episodes) {
            popupSharedViewModel.setEpisodes(it)
        }
        subscribe(viewModel.team1) {
            popupSharedViewModel.setTeam1(it)
        }
        subscribe(viewModel.team2) {
            popupSharedViewModel.setTeam2(it)
        }

        subscribeEvent(popupSharedViewModel.playEpisode) {
            viewModel.play(episode = it)
        }
        subscribeEvent(popupSharedViewModel.playPlayList) {
            viewModel.play(playList = it)
        }


        popupVideoNames = resources.getStringArray(R.array.popup_video_names)
        val popupVideoAdapter = PopupVideoFragmentAdapter(
            childFragmentManager,
            this.lifecycle, popupVideoNames.size
        )
        popup_video_pager.adapter = popupVideoAdapter
        TabLayoutMediator(tab_layout, popup_video_pager, false) { tab, position ->
            tab.text = popupVideoNames[position]
            tab.view.isFocusable = true
        }.attach()
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(PopupVideoFragmentArgs.fromBundle(requireArguments()))
    }


    override fun onStop() {
        super.onStop()
        (activity as MainActivity).main_group?.visibility = View.VISIBLE
        (activity as MainActivity).popup_group?.visibility = View.GONE
        (activity as MainActivity).mainMotion?.predominantColorToGradient("#3560E1")
    }

    inner class PopupVideoFragmentAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        private val itemCount: Int
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return itemCount
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TabWatchFragment()
                1 -> TabAdditionallyFragment()
                2 -> TabByPlayersFragment()
                3 -> TabMatchEventsFragment()
                4 -> TabLanguagesFragment()
                else -> TabWatchFragment()
            }
        }
    }
}