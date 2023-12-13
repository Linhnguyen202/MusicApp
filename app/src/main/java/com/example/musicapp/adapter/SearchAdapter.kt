package com.example.musicapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.MusicItemBinding
import com.example.musicapp.databinding.SeachItemBinding
import com.example.musicapp.databinding.SearchItemNotFoundBinding
import com.example.musicapp.layout.SearchScreen
import com.example.musicapp.model.MainModel
import com.example.musicapp.model.Music

class SearchAdapter(val onClick : (Int,MutableList<Music>)->Unit) : RecyclerView.Adapter<SearchAdapter.SearchHolder>()  {
    val differ = AsyncListDiffer(this, differCallback)
    inner class SearchHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding = SeachItemBinding.bind(itemView)
        fun inject(music: Music,position: Int){
            binding.searchItemTitle.text = music.name_music
            Glide.with(itemView).load(music.image_music).into(binding.songBanner)
            binding.searchItemArtist.text = music.name_singer
            binding.searchItemCard.setOnClickListener {
                onClick(position,differ.currentList)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.seach_item,parent,false)
        return SearchHolder(view)

    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.inject(differ.currentList[position],position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Music>(){
            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem._id == newItem._id

            }

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem._id == newItem._id
            }

        }
    }
}