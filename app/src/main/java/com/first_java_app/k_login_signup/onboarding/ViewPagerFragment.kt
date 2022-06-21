package com.first_java_app.k_login_signup.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.first_java_app.k_login_signup.databinding.FragmentViewPagerBinding
import com.first_java_app.k_login_signup.onboarding.screens.Onboarding
import com.first_java_app.k_login_signup.onboarding.screens.Onboarding1
import com.first_java_app.k_login_signup.onboarding.screens.Onboarding2

class ViewPagerFragment : Fragment() {
    lateinit var binding : FragmentViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerBinding.inflate(inflater,container,false)

        val fragmentList = arrayListOf(
            Onboarding(),
            Onboarding1(),
            Onboarding2()
        )
        val adapter = ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager,lifecycle)
        binding.viewPager.adapter = adapter
        return binding.root
    }

}