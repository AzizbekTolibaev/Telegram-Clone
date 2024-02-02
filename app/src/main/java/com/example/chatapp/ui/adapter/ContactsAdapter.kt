package com.example.chatapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chatapp.data.ContactsData
import com.example.chatapp.databinding.ItemUserBinding

class ContactsAdapter: RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    private var onItemClickListener: ((ContactsData) -> Unit)? = null

    fun setOnItemClickListener(block: (ContactsData) -> Unit) {
        onItemClickListener = block
    }

    inner class ContactsViewHolder(private val binding: ItemUserBinding): ViewHolder(binding.root) {
        fun bind() {
            val d = models[adapterPosition]
            binding.contactName.text = d.name

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(d)
            }
        }
    }

    var models = mutableListOf<ContactsData>()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind()
    }
}

