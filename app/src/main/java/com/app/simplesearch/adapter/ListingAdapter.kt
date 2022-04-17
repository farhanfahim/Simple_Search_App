package com.app.simplesearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.simplesearch.R
import com.app.simplesearch.data.model.Items
import com.app.simplesearch.databinding.ItemRvItemsBinding
import kotlin.collections.ArrayList

class ListingAdapter : RecyclerView.Adapter<ListingAdapter.ItemViewHolder>() {
    private var arrPhoto = ArrayList<Items>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(arrPhoto[position], position)
    }

    fun setData(itemList: List<Items>) {
        arrPhoto = itemList as ArrayList<Items>
    }

    fun clearData() {
        arrPhoto.clear()
    }


    override fun getItemCount() = arrPhoto.size

    inner class ItemViewHolder(
        private val parent: ViewGroup,
        private val binding: ItemRvItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_rv_items,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Items, position: Int) {


            binding.tvLogin.text = "${data.login}"
            binding.tvType.text = "${data.login}"
            binding.imgAvatar.load(data.avatarUrl) {
                placeholder(R.color.colorGray)
                crossfade(true)
            }

        }
    }


}