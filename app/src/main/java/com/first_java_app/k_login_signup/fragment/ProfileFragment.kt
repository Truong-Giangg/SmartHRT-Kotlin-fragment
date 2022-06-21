package com.first_java_app.k_login_signup.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.first_java_app.k_login_signup.R
import com.first_java_app.k_login_signup.model.User
import com.first_java_app.k_login_signup.viewmodel.UserLoginViewModel
import com.first_java_app.k_login_signup.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var sharePreferences : SharedPreferences
    private lateinit var binding : FragmentProfileBinding
    private lateinit var viewModel: UserLoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        sharePreferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserLoginViewModel::class.java)
        val name = sharePreferences.getString("NAME","")
        val email = sharePreferences.getString("EMAIL","")
        val password = sharePreferences.getString("PASSWORD","")
        binding.apply {
            viewModel.user =
                User(name.toString(),email.toString(),password.toString())
            binding.user = viewModel.user
            txtMail.setOnClickListener {
                showDialog(txtMail)
            }
            txtPassword.setOnClickListener {
                showDialog(txtPassword)
            }
            txtName.setOnClickListener {
                showDialog(txtName)
            }
            setLogOut()
        }
    }

    private fun showDialog(txtView : TextView){
        val builder = AlertDialog.Builder(requireContext())
        val dialogLayout = layoutInflater.inflate(R.layout.profile_dialog,null)
        val txtBox =dialogLayout.findViewById<EditText>(R.id.editText)
        txtBox.setText(txtView.text)
        with(builder){
            setTitle("Change ${txtView.hint}")
            setMessage("${txtView.hint}")
            setPositiveButton("Save"){dialog, which ->
                txtView.text = txtBox?.text.toString()
                if(txtView.hint.equals("Full name")){
                    viewModel.user.fullName = txtView.text.toString()
                    binding.invalidateAll()
                }
                else if(txtView.hint.equals("Email")){
                    viewModel.user.email = txtView.text.toString()
                    binding.invalidateAll()
                }
                else if(txtView.hint.equals("Password")){
                    viewModel.user.password = txtView.text.toString()
                    binding.invalidateAll()
                }
                dialog.dismiss()
            }
            setNegativeButton("Cancel"){dialog, which ->
                dialog.dismiss()
            }
            setView(dialogLayout)
            show()
        }
    }
    private fun setLogOut(){
        binding.logoutBtn.setOnClickListener {
            val editor : SharedPreferences.Editor = sharePreferences.edit()
            editor.putString("NAME",viewModel.user.fullName)
            editor.putString("EMAIL",viewModel.user.email)
            editor.putString("PASSWORD",viewModel.user.password.trim())
            editor.putBoolean("CHECK",false)
            editor.apply()
            val controller = findNavController()
            controller.navigate(R.id.action_profileFragment_to_welcomeFragment)
        }
    }
}