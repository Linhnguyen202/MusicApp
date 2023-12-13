package com.example.musicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.PlaylistMusicItemBinding
import com.example.musicapp.databinding.SeachItemBinding
import com.example.musicapp.model.Music
import com.example.musicapp.model.MusicPlaylistData

class MusicPlaylistAdapter (val onClick : (Int,MutableList<MusicPlaylistData>)->Unit,val deleteMusic : (String,String)->Unit) : RecyclerView.Adapter<MusicPlaylistAdapter.MusicPlaylistHolder>()  {
    val differ = AsyncListDiffer(this, differCallback)
    inner class MusicPlaylistHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding = PlaylistMusicItemBinding.bind(itemView)
        fun inject(music: MusicPlaylistData, position: Int){
            binding.searchItemTitle.text = music.music.name_music
            Glide.with(itemView).load(music.music.image_music).into(binding.songBanner)
            binding.searchItemArtist.text = music.music.name_singer
            binding.searchItemCard.setOnClickListener {
                onClick(position,differ.currentList)
            }
            binding.deleteMusicBtn.setOnClickListener {
                deleteMusic.invoke(music.id_list,music.music._id.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicPlaylistHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_music_item,parent,false)
        return MusicPlaylistHolder(view)

    }

    override fun onBindViewHolder(holder: MusicPlaylistHolder, position: Int) {
        holder.inject(differ.currentList[position],position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<MusicPlaylistData>(){
            override fun areItemsTheSame(oldItem: MusicPlaylistData, newItem: MusicPlaylistData): Boolean {
                return oldItem._id == newItem._id

            }

            override fun areContentsTheSame(oldItem: MusicPlaylistData, newItem: MusicPlaylistData): Boolean {
                return oldItem._id == newItem._id
            }

        }
    }
}