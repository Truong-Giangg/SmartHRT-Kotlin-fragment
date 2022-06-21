package com.first_java_app.k_login_signup.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.first_java_app.k_login_signup.DataStore
import com.first_java_app.k_login_signup.FireBase
import com.first_java_app.k_login_signup.R
import com.first_java_app.k_login_signup.UserHelperClass
import com.first_java_app.k_login_signup.viewmodel.UserLoginViewModel
import com.first_java_app.k_login_signup.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private lateinit var viewModel: UserLoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserLoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            gotoLogin.setOnClickListener {
                val controller = findNavController()
                controller.navigate(R.id.action_signUpFragment_to_signInFragment)
            }
            btnSignUp.setOnClickListener {
                // code nhận thông tin đăng kí ở đây
                viewModel.checkEmailAndPassword(
                    inputEmail.text.toString().trim(),
                    inputPass.text.toString().trim()
                )
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listenerSuccessEvent()
        listenerErrorEvent()
    }
    private fun listenerSuccessEvent() {
        viewModel.isSuccessEvent.observe(viewLifecycleOwner) {
            if (it) {
                DataStore(binding.inputFullName.text.toString().trim(),
                    binding.inputEmail.text.toString().trim(),
                    binding.inputPass.text.toString().trim())
                Log.e("SignUpFragment:", " mk = ${binding.inputPass.text.toString().trim()}");
                var helperClass = UserHelperClass(binding.inputFullName.text.toString().trim(), "giang123vn1", binding.inputEmail.text.toString().trim(),"01234555",binding.inputPass.text.toString().trim())
                var fData = FireBase()
                fData.database.reference.child("giang123vn1").setValue(helperClass)
                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
//                findNavController().popBackStack()
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