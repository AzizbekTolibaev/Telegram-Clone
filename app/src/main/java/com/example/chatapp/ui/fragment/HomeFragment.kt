package com.example.chatapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.chatapp.MainActivity
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentHomeBinding
import com.example.chatapp.ui.adapter.viewpageradapter.FragmentPageAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import net.cachapa.expandablelayout.ExpandableLayout

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var fragmentAdapter: FragmentPageAdapter
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        initVariables()
        initListener()

        binding.imgSearch.setOnClickListener {
            auth.signOut()
            val shPref = requireActivity().getSharedPreferences("shPref", Context.MODE_PRIVATE)
            shPref.edit().putBoolean("isActive", false).apply()
            val intent = Intent(requireContext(), MainActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }

    }

    private fun initVariables() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),
            R.color.toolbar_color
        )
        tabLayoutAndViewPager()
        drawerLayoutListener()
    }

    private fun initListener() {
        binding.openDrawer.setOnClickListener {
            binding.drawerLayout.open()
        }
    }

    private fun drawerLayoutListener() {
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu_new_group -> Toast.makeText(requireContext(), it.title, Toast.LENGTH_SHORT).show()
                R.id.menu_contacts -> Toast.makeText(requireContext(), "Contacts", Toast.LENGTH_SHORT).show()
                R.id.menu_calls -> Toast.makeText(requireContext(), "Calls", Toast.LENGTH_SHORT).show()
                R.id.menu_people_nearby -> Toast.makeText(requireContext(), "People Nearby", Toast.LENGTH_SHORT).show()
                R.id.menu_saved_messages -> Toast.makeText(requireContext(), "Saved Messages", Toast.LENGTH_SHORT).show()
                R.id.menu_setting -> Toast.makeText(requireContext(), "Setting", Toast.LENGTH_SHORT).show()
                R.id.menu_invite_friends -> Toast.makeText(requireContext(), "Invite Friends", Toast.LENGTH_SHORT).show()
                R.id.menu_telegram_features -> Toast.makeText(requireContext(), "Telegram Features", Toast.LENGTH_SHORT).show()
            }
            true
        }

        val headerLayout = binding.navView.getHeaderView(0)

        val expandableLayout = headerLayout.findViewById<ExpandableLayout>(R.id.expanded_account_add)
        headerLayout.findViewById<ImageView>(R.id.img_ex_accounts).setOnClickListener {
            if (expandableLayout.isExpanded){
                expandableLayout.collapse()
                headerLayout.findViewById<ImageView>(R.id.img_ex_accounts).setImageResource(R.drawable.ic_down)
            }else{
                expandableLayout.expand()
                headerLayout.findViewById<ImageView>(R.id.img_ex_accounts).setImageResource(R.drawable.ic_up)
            }
        }
    }

    private fun tabLayoutAndViewPager() {
        val tabLayout = binding.tabLayout
        val viewPager2 = binding.viewPager

        fragmentAdapter = FragmentPageAdapter((requireActivity() as MainActivity).supportFragmentManager, lifecycle)

        viewPager2.setPageTransformer(null)

        tabLayout.addTab(tabLayout.newTab().setText("Contacts"))
        tabLayout.addTab(tabLayout.newTab().setText("Groups"))

        viewPager2.adapter = fragmentAdapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}
