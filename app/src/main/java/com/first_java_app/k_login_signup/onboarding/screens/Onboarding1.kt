package com.first_java_app.k_login_signup.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.first_java_app.k_login_signup.R
import com.first_java_app.k_login_signup.databinding.FragmentOnboarding1Binding

class Onboarding1 : Fragment() {

    lateinit var binding : FragmentOnboarding1Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboarding1Binding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.nextBtn.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }
}