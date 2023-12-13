package com.example.musicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.MusicItemBinding
import com.example.musicapp.model.Music

class MusicAdapter(val onClick : (Int,MutableList<Music>) -> Unit)  : RecyclerView.Adapter<MusicAdapter.MovieViewHolder>() {
    val differ = AsyncListDiffer(this,differCallback)
    inner class MovieViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding = MusicItemBinding.bind(itemView)

        fun inject(music: Music, position: Int){
            binding.songTitle.text = music.name_music
            Glide.with(itemView).load(music.image_music).into(binding.imgMusicPoster)
            binding.songCard.setOnClickListener {
                val list : MutableList<Music> = differ.currentList
                onClick(position ,differ.currentList)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_item,parent,false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
      holder.inject(differ.currentList[position],position)
    }



    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Music>(){
            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem == newItem

            }

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem == newItem
            }

        }
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}