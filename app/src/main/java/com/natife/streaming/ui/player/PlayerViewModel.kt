package com.natife.streaming.ui.player

import com.natife.streaming.base.BaseViewModel

abstract class PlayerViewModel : BaseViewModel() {
    abstract fun showVideos()
}

class PlayerViewModelImpl : PlayerViewModel() {
    override fun showVideos() {
        TODO("Not yet implemented")
    }
}