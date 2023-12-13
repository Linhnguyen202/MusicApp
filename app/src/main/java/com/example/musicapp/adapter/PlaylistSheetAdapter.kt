package com.example.musicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.databinding.CustomPlaylistSheetItemBinding
import com.example.musicapp.databinding.ParentItemBinding
import com.example.musicapp.layout.PlaylistSheet
import com.example.musicapp.model.MainModel
import com.example.musicapp.model.Playlist

class PlaylistSheetAdapter(val onClick : (Playlist)->Unit) : RecyclerView.Adapter<PlaylistSheetAdapter.PlaylistSheetViewHolder>(){
    inner class PlaylistSheetViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding = CustomPlaylistSheetItemBinding.bind(itemView)
        fun inject(playlist: Playlist){
            binding.playlistItemTitle.text = playlist.name_list
            binding.itemContainer.setOnClickListener {
                onClick(playlist)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_playlist_sheet_item,parent,false)
        return PlaylistSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistSheetViewHolder, position: Int) {
        holder.inject(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    val differ = AsyncListDiffer(this, PlaylistSheetAdapter.differCallback)
    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Playlist>(){
            override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem == newItem

            }

            override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem == newItem
            }

        }
    }
}