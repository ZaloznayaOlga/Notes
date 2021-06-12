package com.natife.streaming.ui.popupmatch.statistics

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.bindTeamImage
import com.natife.streaming.ext.predominantColorToGradient
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.main.MainActivity
import com.natife.streaming.ui.popupmatch.PopupSharedViewModel
import com.natife.streaming.ui.popupmatch.statistics.commands.TabCommandsFragment
import com.natife.streaming.ui.popupmatch.statistics.table.TabTableFragment
import com.natife.streaming.ui.popupmatch.statistics.team.TabTeam1Fragment
import com.natife.streaming.ui.popupmatch.statistics.team.TabTeam2Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_popup_statistics.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf


class PopupStatisticsFragment : BaseFragment<PopupStatisticsViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_popup_statistics
    private var popupStatisticNames = arrayListOf<String>()
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
            text = resources.getString(R.string.video)
        }
        subscribe(popupSharedViewModel.match) {
            (activity as MainActivity).logo_first_team?.bindTeamImage(it.sportId, it.team1.id)
            (activity as MainActivity).name_first_team?.text = it.team1.name.take(3)
            (activity as MainActivity).logo_second_team?.bindTeamImage(it.sportId, it.team2.id)
            (activity as MainActivity).name_second_team?.text = it.team2.name.take(3)
            (activity as MainActivity).score_text?.text = "${it.team1.score} - ${it.team2.score}"

            popupStatisticNames.add(resources.getString(R.string.teams))
            popupStatisticNames.add(it.team1.name.take(3))
            popupStatisticNames.add(it.team2.name.take(3))
            popupStatisticNames.add(resources.getString(R.string.table))
            val popupStatisticsAdapter =
                PopupStatisticsFragmentAdapter(requireActivity(), popupStatisticNames.size)
            popup_stat_pager.adapter = popupStatisticsAdapter
            TabLayoutMediator(tab_layout_stat, popup_stat_pager) { tab, position ->
                tab.text = popupStatisticNames[position]
            }.attach()
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).main_group?.visibility = View.VISIBLE
        (activity as MainActivity).popup_group?.visibility = View.GONE
        (activity as MainActivity).mainMotion?.predominantColorToGradient("#3560E1")
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(PopupStatisticsFragmentArgs.fromBundle(requireArguments()))
    }

    inner class PopupStatisticsFragmentAdapter(
        activity: FragmentActivity,
        private val itemCount: Int
    ) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return itemCount
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TabCommandsFragment()
                1 -> TabTeam1Fragment()
                2 -> TabTeam2Fragment()
                3 -> TabTableFragment()
                else -> {
                    TabCommandsFragment()
                }
            }
        }
    }
}

