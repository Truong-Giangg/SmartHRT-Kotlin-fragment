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
import com.first_java_app.k_login_signup.*
import com.first_java_app.k_login_signup.R
import com.first_java_app.k_login_signup.databinding.FragmentSignInBinding
import com.first_java_app.k_login_signup.model.User
import com.first_java_app.k_login_signup.viewmodel.UserLoginViewModel
import com.google.firebase.database.*
import org.opencv.android.OpenCVLoader

class SignInFragment : Fragment() {
    private lateinit var sharePreferences : SharedPreferences
    private lateinit var binding : FragmentSignInBinding
    private lateinit var viewModel: UserLoginViewModel
    var rootNode: FirebaseDatabase? = null
    var reference: DatabaseReference? = null
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
                isUser()
            }

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
    }


    private fun isUser() {
        val userEnteredUsername: String = binding.inputUser.text.toString().trim()
        val userEnteredPassword: String = binding.inputPass.text.toString().trim()

        val reference = FirebaseDatabase.getInstance().getReference("users")
        val checkUser = reference.orderByChild("username").equalTo(userEnteredUsername)
        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    binding.inputUser.setError(null)
                    val passwordFromDB =
                        dataSnapshot.child(userEnteredUsername).child("password").getValue(
                            String::class.java
                        )
                    // tro toi urename va tim password
                    if (passwordFromDB == userEnteredPassword) {
                        binding.inputUser.setError(null)
                        MainActivity.user_username_gadget = binding.inputUser.text.toString().trim()
                        binding.inputPass.setText("")
                        val intent = Intent(activity, MainMenu::class.java)
                        startActivity(intent)
                    } else {
                        binding.inputPass.setError("Wrong Password")
                        binding.inputUser.requestFocus()
                    }
                } else {
                    binding.inputUser.setError("No such user exit")
                    binding.inputUser.requestFocus()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}