package com.first_java_app.k_login_signup.onboarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.first_java_app.k_login_signup.R
import com.first_java_app.k_login_signup.databinding.FragmentOnboarding2Binding

class Onboarding2 : Fragment() {
    lateinit var binding : FragmentOnboarding2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboarding2Binding.inflate(inflater,container,false)
        return binding.root
    }
    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished",true)
        editor.apply()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextBtn.setOnClickListener {
            onBoardingFinished()
            findNavController().navigate(R.id.action_viewPagerFragment_to_welcomeFragment)
        }
    }
}