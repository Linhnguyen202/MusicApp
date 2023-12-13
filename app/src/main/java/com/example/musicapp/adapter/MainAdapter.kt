package com.example.musicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.databinding.MusicItemBinding
import com.example.musicapp.databinding.ParentItemBinding
import com.example.musicapp.model.MainModel
import com.example.musicapp.model.Music

class MainAdapter(val onClick : (Int,MutableList<Music>)->Unit) : RecyclerView.Adapter<MainAdapter.MainHolder>() {
    val differ = AsyncListDiffer(this,differCallback)
    inner class MainHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding = ParentItemBinding.bind(itemView)
        fun inject(mainModel: MainModel){
            binding.titleMusicCate.text = mainModel.title
            val musicAdapter : MusicAdapter = MusicAdapter(onClick)
            musicAdapter.differ.submitList(mainModel.musicModels)
            binding.rvMusicChild.apply {
                this.adapter = musicAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.parent_item,parent,false)
        return MainHolder(view)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.inject(differ.currentList[position])
    }



    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<MainModel>(){
            override fun areItemsTheSame(oldItem: MainModel, newItem: MainModel): Boolean {
                return oldItem == newItem

            }

            override fun areContentsTheSame(oldItem: MainModel, newItem: MainModel): Boolean {
                return oldItem == newItem
            }

        }
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}