package com.first_java_app.k_login_signup.fragment

//import com.first_java_app.k_login_signup.FireBase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.first_java_app.k_login_signup.DataStore
import com.first_java_app.k_login_signup.R
import com.first_java_app.k_login_signup.databinding.FragmentSignUpBinding
import com.first_java_app.k_login_signup.model.UserHelperClass
import com.first_java_app.k_login_signup.viewmodel.UserLoginViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private lateinit var viewModel: UserLoginViewModel
    var rootNode: FirebaseDatabase? = null
    var reference: DatabaseReference? = null
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
                var name = binding.inputFullName.text.toString().trim()
                var username = binding.inputFullName.text.toString().trim()
                var email = binding.inputEmail.text.toString().trim()
                var phoneNo = binding.inputPhone.text.toString().trim()
                var password = binding.inputPass.text.toString().trim()
                DataStore(username, email, password)
                Log.e("SignUpFragment:", " mk = ${binding.inputPass.text.toString().trim()}");
                rootNode = FirebaseDatabase.getInstance()
                reference = rootNode!!.getReference("users")
                val helperClass = UserHelperClass(name, username, email, phoneNo, password)
                reference!!.child(username).setValue(helperClass)

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