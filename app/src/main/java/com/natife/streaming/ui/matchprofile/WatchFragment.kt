package com.natife.streaming.ui.matchprofile

import android.os.Bundle
import android.view.View
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.toDisplayTime
import kotlinx.android.synthetic.main.fragment_match_prewatch.*

import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class WatchFragment: BaseFragment<WatchViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_match_prewatch
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(viewModel.title,title::setText)
        buttonBack.setOnClickListener {
            viewModel.back()
        }
        subscribe(viewModel.startFrom){ sec ->
            watchFromMoment.text = "Смотреть с ${sec.first.toDisplayTime()}"//TODO multilang
            watchFromMoment.setOnClickListener {
                                            viewModel.toPlayer(Episode(
                                                half = sec.second.half,
                                                start = sec.second.second,
                                                end = -1
                                            ))
            }
        }
        watchFromStart.setOnClickListener {
            viewModel.toPlayer(Episode(
                half = 1,
                start = -1,
                end = -1
            ))
        }

    }
    override fun getParameters(): ParametersDefinition = {
        parametersOf(WatchFragmentArgs.fromBundle(requireArguments()))
    }
}