package com.mihir.swipetask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mihir.swipetask.common.loadImg
import com.mihir.swipetask.data.ProductListItem
import com.mihir.swipetask.databinding.ItemSampleBinding

class AdapterProduct : ListAdapter<ProductListItem, AdapterProduct.ViewHolder>(ItemCallback) {

    object ItemCallback : DiffUtil.ItemCallback<ProductListItem>() {
        override fun areItemsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean =
            oldItem.product_name == newItem.product_name

        override fun areContentsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean =
            oldItem == newItem
    }

    var accessoryItemData: List<ProductListItem> = emptyList()
        set(value) {
            field = value
            onListOrFilterChange()
        }

    var filter: CharSequence = ""
        set(value) {
            field = value
            onListOrFilterChange()
        }

    inner class ViewHolder(private val itemBinding: ItemSampleBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item:ProductListItem) {
            with(itemBinding) {
                txtVProdName.text = item.product_name
                txtVPrice.text = "₹${item.price}"
                txtVTax.text = "Tax:${item.tax.toString()}"
                chipProdType.text = item.product_type
                imgVProduct.loadImg(item.image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    private fun onListOrFilterChange() {
        if (filter.length < 2) {
            submitList(accessoryItemData)
            return
        }
        val pattern = filter.toString().lowercase().trim()
        val filteredList = accessoryItemData.filter { pattern in it.product_name.lowercase() }
        submitList(filteredList)
    }

}