package com.example.chatapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.chatapp.R
import com.example.chatapp.data.ContactsData
import com.example.chatapp.databinding.FragmentContactsBinding
import com.example.chatapp.ui.adapter.ContactsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContactsFragment: Fragment(R.layout.fragment_contacts) {
    private lateinit var binding: FragmentContactsBinding
    private val adapter = ContactsAdapter()
    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactsBinding.bind(view)

        binding.rcView.adapter = adapter

        binding.rcView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        adapter.setOnItemClickListener {
            val id = it.id.toString()
            val name = it.name.toString()
            findNavController().navigate(HomeFragmentDirections.actionHomeFragment2ToChatFragment(id, name))
        }

        val database = FirebaseDatabase.getInstance()

        database.getReference("users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val list = mutableListOf<ContactsData>()
                    snapshot.children.forEach{ data ->
                        val item = data.value as HashMap<*, *>
                        val user = data.getValue(ContactsData::class.java)
                        if (auth.currentUser?.uid != item["id"].toString()) {
                            list.add(user!!)
                        }
                    }

                    adapter.models = list
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })

    }
}
