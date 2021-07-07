package com.example.videolist.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.videolist.models.Video
import com.example.videolist.databinding.ItemVideoListBinding
import com.example.videolist.ui.main.list.OnItemClickListener

//Adapter for recyclerview
class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var listener : OnItemClickListener

    private var videos: List<Video> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemVideoListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyViewHolder).bind(videos[position])
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    fun setVideos(list: List<Video>) {
        val diffResult = DiffUtil.calculateDiff(VideoItemDiffCallback(videos, list))
        videos = list
        diffResult.dispatchUpdatesTo(this)
    }

    //Increase recyclerView performance with diffUtil
    class VideoItemDiffCallback(
        private val oldList: List<Video>,
        private val newList: List<Video>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].video_name == newList[newItemPosition].video_name
        }

    }

    inner class MyViewHolder constructor(private val binding: ItemVideoListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(value: Video) {
            val bMap = BitmapFactory.decodeFile(value.image_path)
            binding.imageView.setImageBitmap(bMap)

            itemView.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }
    }
}