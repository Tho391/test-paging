package com.thomas.apps.testpaging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thomas.apps.testpaging.api.OfficeResponseItem
import com.thomas.apps.testpaging.databinding.ItemOfficeBinding
import com.thomas.apps.testpaging.model.Office

class OfficeAdapter :
    PagingDataAdapter<Office, OfficeAdapter.ViewHolder>(OfficeResponseItemDC()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ViewHolder private constructor(private val binding: ItemOfficeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                Toast.makeText(binding.root.context, "You click", Toast.LENGTH_SHORT).show()
            }
        }

        fun bind(item: Office?) {
            item?.let {
                binding.office = item
                binding.executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemOfficeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    private class OfficeResponseItemDC : DiffUtil.ItemCallback<Office>() {
        override fun areItemsTheSame(
            oldItem: Office,
            newItem: Office
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Office,
            newItem: Office
        ): Boolean {
            return oldItem == newItem
        }
    }
}