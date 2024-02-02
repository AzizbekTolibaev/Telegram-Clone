package com.example.chatapp.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.chatapp.R
import com.example.chatapp.data.TheyMessage
import com.example.chatapp.databinding.FragmentChatBinding
import com.example.chatapp.ui.adapter.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stdio.swipetoreply.SwipeController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatFragment: Fragment(R.layout.fragment_chat) {
    private lateinit var binding: FragmentChatBinding
    private val adapter = MessageAdapter()
    private val database = FirebaseDatabase.getInstance()
    private val args: ChatFragmentArgs by navArgs()
    private var senderRoom: String = ""
    private var receiverRoom: String = ""
    private val messagesList = mutableListOf<Any>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)
        binding.rcView.smoothScrollToPosition(messagesList.size)

        swipeToReply()

        val receiverId = args.id
        val name = args.name
        val senderId = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = senderId + "And" + receiverId
        receiverRoom = receiverId + "And" + senderId

        binding.tvUserName.text = name
        binding.rcView.adapter = adapter

        binding.etMessage.setOnClickListener {
            binding.rcView.smoothScrollToPosition(messagesList.size)
        }

        binding.etMessage.addTextChangedListener {
            val message = it.toString().trim()
            if (message.isNotEmpty()) {
                binding.apply {
                    imgSend.visibility = View.VISIBLE
                    imgFile.visibility = View.GONE
                    imgMic.visibility = View.GONE
                }
            } else {
                binding.apply {
                    imgSend.visibility = View.GONE
                    imgFile.visibility = View.VISIBLE
                    imgMic.visibility = View.VISIBLE
                }
            }
        }

        database.getReference("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    messagesList.clear()

                    snapshot.children.forEach{ data ->

                        val message = data.getValue(com.example.chatapp.data.Message::class.java)
                        message?.let {
                            val theyMessage = data.getValue(TheyMessage::class.java)
                            if (message.senderId == senderId) {
                                messagesList.add(message)
                            } else {
                                messagesList.add(theyMessage!!)
                            }
                        }
                    }
                    binding.rcView.smoothScrollToPosition(messagesList.size)
                    adapter.models = messagesList
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        binding.imgSend.setOnClickListener {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val formattedDateTime = currentDateTime.format(formatter)
            val time = System.currentTimeMillis()
            val message = binding.etMessage.text.toString().trim()
            val messageObject = com.example.chatapp.data.Message(message, senderId, formattedDateTime)

            database.getReference("chats").child(senderRoom).child("messages").child("$time")
                .setValue(messageObject).addOnSuccessListener {

                    database.getReference("chats").child(receiverRoom).child("messages").child("$time")
                        .setValue(messageObject)
                }

            binding.rcView.smoothScrollToPosition(messagesList.size)
            binding.etMessage.setText("")
        }
    }

    private fun swipeToReply() {
        val controller = SwipeController(requireActivity()) {
            binding.etMessage.requestFocus()
            showKeyboard()

            binding.layoutReplyMessage.visibility = View.VISIBLE
            binding.replyClose.setOnClickListener {
                binding.layoutReplyMessage.visibility = View.GONE
            }

        }
        val itemTouchHelper = ItemTouchHelper(controller)
        itemTouchHelper.attachToRecyclerView(binding.rcView)
    }

    private fun Fragment.showKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun Fragment.hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = requireActivity().currentFocus
        if (currentFocusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
        }
    }

    fun Fragment.isKeyboardOpen(): Boolean {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.isActive
    }
}
