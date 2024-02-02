package com.example.chatapp.ui.adapter.viewpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chatapp.ui.fragment.ContactsFragment
import com.example.chatapp.ui.fragment.GroupsFragment

class FragmentPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            ContactsFragment()
        } else {
            GroupsFragment()
        }
    }
}
