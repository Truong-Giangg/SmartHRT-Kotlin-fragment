package com.first_java_app.k_login_signup.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.first_java_app.k_login_signup.R
import com.first_java_app.k_login_signup.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    lateinit var binding : FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater,container,false)
        Handler().postDelayed({
            if(onBoardingFinished()) {// true
                findNavController().navigate(R.id.action_splash_to_welcomeFragment)
            }
            else{
                findNavController().navigate(R.id.action_splash_to_viewPagerFragment)
            }
        }, 2000) // 2secs
        return binding.root
    }
    private fun onBoardingFinished() : Boolean {
        val sharePref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharePref.getBoolean("Finished",false)
    }

}