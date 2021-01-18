package com.natife.streaming.ui.main

import android.os.Bundle
import android.view.KeyEvent
import androidx.constraintlayout.motion.widget.MotionLayout
import com.natife.streaming.R
import com.natife.streaming.base.BaseActivity
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel>() {

    override fun getLayoutRes() = R.layout.activity_main

    override fun getNavHostId() = R.id.globalNavFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainMenu.setRouter(router)

        subscribe(viewModel.name){
            mainMenu.setProfile(it)
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (mainMenu.hasFocus()) {
            (mainMotion as MotionLayout).transitionToEnd()
        } else {
            (mainMotion as MotionLayout).transitionToStart()
        }
        return super.dispatchKeyEvent(event)
    }
}