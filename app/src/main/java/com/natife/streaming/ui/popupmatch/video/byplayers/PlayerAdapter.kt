package com.natife.streaming.ui.popupmatch.video.byplayers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.card.MaterialCardView
import com.natife.streaming.R
import com.natife.streaming.base.BaseListAdapter
import com.natife.streaming.data.matchprofile.Player
import kotlinx.android.synthetic.main.item_player_new.view.*

class PlayerAdapter () : BaseListAdapter<Player, PlayerViewHolder>(PlayerDiffCallback()) {
    var onClick: ((Player)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player_new, parent, false)
                .apply {
                    //layoutParams = ViewGroup.LayoutParams(itemWidth,ViewGroup.LayoutParams.WRAP_CONTENT)
                }
        )
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentList[position])
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewAttachedToWindow(holder: PlayerViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            (view as MaterialCardView).player_card_background.background = if (hasFocus) {
                view.context.resources.getDrawable(R.drawable.background_button_fild_white, null)
            } else {
                view.context.resources.getDrawable(R.drawable.background_button_fild_grey, null)
            }
            (view as MaterialCardView).player_name.setTextColor(
                if (hasFocus) {
                    view.context.resources.getColor(R.color.black, null)
                } else {
                    view.context.resources.getColor(R.color.white, null)
                }
            )
            (view as MaterialCardView).player_time.setTextColor(
                if (hasFocus) {
                    view.context.resources.getColor(R.color.gray, null)
                } else {
                    view.context.resources.getColor(R.color.white40, null)
                }
            )
        }
    }
}

class PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem == newItem
    }

}