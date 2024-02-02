package com.example.chatapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chatapp.R
import com.example.chatapp.data.Message
import com.example.chatapp.data.TheyMessage
import com.example.chatapp.databinding.ItemMessageMeBinding
import com.example.chatapp.databinding.ItemMessageTheyBinding

class MessageAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var onSwipeToReply: ((Message) -> Unit)? = null

    fun setOnSwipeToReply(block: (Message) -> Unit) {
        onSwipeToReply = block
    }

    inner class MeViewHolder(private val binding: ItemMessageMeBinding): ViewHolder(binding.root) {
        fun bind(me: Message) {
            binding.tvMessage.text = me.message
            binding.tvTime.text = me.time

        }
    }

    inner class TheyViewHolder(private val binding: ItemMessageTheyBinding): ViewHolder(binding.root) {
        fun bind(they: TheyMessage) {
            binding.tvMessage.text = they.message
            binding.tvTime.text = they.time
        }
    }

    var models = mutableListOf<Any>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (models[position] is Message) 1 else 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_me, parent, false)
                val binding = ItemMessageMeBinding.bind(view)
                MeViewHolder(binding)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_they, parent, false)
                val binding = ItemMessageTheyBinding.bind(view)
                TheyViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            1 -> {
                (holder as MeViewHolder).bind(models[position] as Message)
            }
            else -> {
                (holder as TheyViewHolder).bind(models[position] as TheyMessage)
            }
        }
    }
}
