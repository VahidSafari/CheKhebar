package com.example.chekhebar.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chekhebar.R
import com.example.chekhebar.databinding.ItemPlaceBinding

private object PlacesDiffUtillCallback : DiffUtil.ItemCallback<PlaceView>() {
    override fun areItemsTheSame(oldItem: PlaceView, newItem: PlaceView): Boolean {
        return (oldItem.id == newItem.id)
    }

    override fun areContentsTheSame(oldItem: PlaceView, newItem: PlaceView): Boolean {
        return (oldItem.name == oldItem.name &&
                oldItem.distance == oldItem.distance
                )
    }

}

class PlaceAdapter :
    ListAdapter<PlaceView, PlaceAdapter.ViewHolder>(
        PlacesDiffUtillCallback
    ) {

    inner class ViewHolder(icBinding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(icBinding.root) {
        internal val holderBinding = icBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemPlaceBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_place, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.tag = getItem(position)
        holder.holderBinding.placeView = item
    }
}