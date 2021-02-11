package com.natife.streaming.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.DiffUtil
import com.natife.streaming.R
import com.natife.streaming.base.BaseListAdapter
import com.natife.streaming.data.matchprofile.Episode

class PlaylistAdapter(inline val onClick:((Episode)->Unit)): BaseListAdapter<Episode, PlaylistViewHolder>(PlaylistDiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_episode,parent,false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            onClick.invoke(currentList[position])
        }
    }
}
class PlaylistDiffUtils: DiffUtil.ItemCallback<Episode>() {
    override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return false
    }

}