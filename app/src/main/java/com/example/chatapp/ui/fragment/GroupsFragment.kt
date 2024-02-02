package com.example.chatapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.chatapp.ui.adapter.ContactsAdapter
import com.example.chatapp.R
import com.example.chatapp.data.ContactsData
import com.example.chatapp.databinding.FragmentGroupsBinding

class GroupsFragment: Fragment(R.layout.fragment_groups) {
    private lateinit var binding: FragmentGroupsBinding
    private val adapter = ContactsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupsBinding.bind(view)

        val data = mutableListOf<ContactsData>()

        repeat(20) {
            data.add(ContactsData("$it", "Android chat", "Islam | yusupbaev: Awa"))
        }

        binding.rcView.adapter = adapter

        adapter.models = data

        binding.rcView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

    }
}