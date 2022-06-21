package com.first_java_app.k_login_signup.fragment

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.first_java_app.k_login_signup.*
import com.first_java_app.k_login_signup.databinding.FragmentMenuBinding
import com.first_java_app.k_login_signup.model.MenuViewModel
import com.first_java_app.k_login_signup.model.Restaurant
import com.first_java_app.k_login_signup.adapter.RestaurantAdapter

class Menu : Fragment(), RestaurantAdapter.OnItemClickListener {
    private lateinit var binding : FragmentMenuBinding
    private lateinit var viewModel : MenuViewModel
    private lateinit var adapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        setupMenu()
        registerData()
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        viewModel.loadData()
    }
    private fun registerData(){
        viewModel.listOfData.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }
    private fun setupMenu(){
        adapter = RestaurantAdapter(this)
        val lm = LinearLayoutManager(requireContext())
        binding.rvMenuList.layoutManager = lm
        binding.rvMenuList.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openProfile()
    }
    private fun openProfile(){
        binding.ivProfile.setOnClickListener {
            val controller = findNavController()
            controller.navigate(R.id.action_menuFragment_to_profileFragment)
        }
    }

    override fun onItemClick(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogLayout =layoutInflater.inflate(R.layout.item_clicked,null)
        val tvName = dialogLayout.findViewById<TextView>(R.id.txtRestaurantName)
        val tvAddress = dialogLayout.findViewById<TextView>(R.id.txtRestaurantAddr)
        val ivImage = dialogLayout.findViewById<ImageView>(R.id.imgRestaurant)
        tvName.text = RestaurantStore.getDataset()[position].name
        tvAddress.text = RestaurantStore.getDataset()[position].address
        Glide.with(dialogLayout).load(RestaurantStore.getDataset()[position].image).into(ivImage)
        with(builder){
            setTitle("Delete item")
            setMessage("Do you want to delete this item?")
            setPositiveButton("Delete"){dialog, which ->
                val restaurant : ArrayList<Restaurant>  = RestaurantStore.getDataset()
                restaurant.removeAt(position)
                adapter.submitList(restaurant)
                adapter.notifyItemRemoved(position)
                Toast.makeText(requireContext(),"item deleted", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            setNegativeButton("Cancel"){dialog, which ->
                dialog.dismiss()
            }
            setView(dialogLayout)
            show()
        }
    }

}