package com.first_java_app.k_login_signup.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.first_java_app.k_login_signup.R
import com.first_java_app.k_login_signup.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    lateinit var binding : FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater,container,false)
        if(onLoginSuccess()){
            findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply{
//            btnSignUp.setOnClickListener {
//                val controller = findNavController()
//                controller.navigate(R.id.action_welcomeFragment_to_signUpFragment)
//            }
            gotoLogin.setOnClickListener {
                val controller = findNavController()
                controller.navigate(R.id.action_welcomeFragment_to_signInFragment)
            }
        }
    }
    private fun onLoginSuccess() : Boolean{
        val sharePreferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        return sharePreferences.getBoolean("CHECK",false)
    }

}