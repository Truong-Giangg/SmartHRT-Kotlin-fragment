package com.first_java_app.k_login_signup.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.first_java_app.k_login_signup.model.User
import com.first_java_app.k_login_signup.*
import com.first_java_app.k_login_signup.databinding.FragmentSignInBinding
import com.first_java_app.k_login_signup.viewmodel.UserLoginViewModel
import org.opencv.android.OpenCVLoader

class SignInFragment : Fragment() {
    private lateinit var sharePreferences : SharedPreferences
    private lateinit var binding : FragmentSignInBinding
    private lateinit var viewModel: UserLoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(UserLoginViewModel::class.java)
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        sharePreferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = sharePreferences.getString("NAME","")
        val email = sharePreferences.getString("EMAIL","")
        val password = sharePreferences.getString("PASSWORD","")
        val userPreferences = User(name.toString(),email.toString(),password.toString())
        if(userPreferences.email.equals(""))
            viewModel.user = DataStore("","","")
        else
            viewModel.user = userPreferences
        binding.apply {
            loginBtn.setOnClickListener {
//                viewModel.checkEmailAndPassword(
//                    inputEmail.text.toString().trim(),
//                    inputPass.text.toString().trim()
//                )
                val intent = Intent(activity, CameraActivity::class.java)
                startActivity(intent)
            }
            listenerSuccessEvent()
            listenerErrorEvent()
        }
        binding.gotoSignup.setOnClickListener {
            val controller = findNavController()
            controller.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        if(OpenCVLoader.initDebug()){
            println("giang-opencv done!")
        }else{
            println("giang-opencv failed!")
        }
        val setdata = FireBase()
//        setdata.setFData("message1","blablabla")
//        println("giangne"+setdata.getFData("message"))
    }

    private fun listenerSuccessEvent() {
        viewModel.isSuccessEvent.observe(viewLifecycleOwner) {
            if (it) {
                var fullName = viewModel.user.fullName
                val editor : SharedPreferences.Editor = sharePreferences.edit()
                editor.putString("NAME",fullName)
                editor.putString("EMAIL",binding.inputEmail.text.toString().trim())
                editor.putString("PASSWORD",binding.inputPass.text.toString().trim())
                editor.putBoolean("CHECK",true)
                editor.apply()
//                val controller = findNavController()
//                controller.navigate(R.id.action_signInFragment_to_homeFragment)





            }
        }
    }

    private fun listenerErrorEvent() {
        viewModel.isErrorEvent.observe(viewLifecycleOwner) { errMess ->
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle("Error")
            dialog.setMessage(errMess)
            dialog.show()
        }
    }
}