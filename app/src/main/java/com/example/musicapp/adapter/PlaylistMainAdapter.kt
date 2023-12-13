package com.example.musicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.databinding.CustomPlaylistSheetItemBinding
import com.example.musicapp.databinding.CustomUserPlaylistMenuBinding
import com.example.musicapp.model.Playlist

class PlaylistMainAdapter(val deletePlaylist : (Playlist)->Unit,val openEditForm : (Playlist) -> Unit,val openPlaylist : (Playlist) -> Unit) : RecyclerView.Adapter<PlaylistMainAdapter.PlaylistMainViewHolder>(){
    inner class PlaylistMainViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding = CustomUserPlaylistMenuBinding.bind(itemView)
        fun inject(playlist: Playlist){
            binding.playlistItemTitle.text = playlist.name_list
            binding.deleteBtn.setOnClickListener {
                deletePlaylist.invoke(playlist)
            }
            binding.editBtn.setOnClickListener {
                openEditForm.invoke(playlist)
            }
            binding.itemContainer.setOnClickListener {
                openPlaylist.invoke(playlist)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistMainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_user_playlist_menu,parent,false)
        return PlaylistMainViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistMainViewHolder, position: Int) {
        holder.inject(differ.currentList[position])
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }
    val differ = AsyncListDiffer(this, PlaylistMainAdapter.differCallback)
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