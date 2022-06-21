package com.first_java_app.k_login_signup

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle

import android.widget.Button

import android.widget.EditText
import android.widget.TextView

import androidx.fragment.app.DialogFragment

class Mydialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder= AlertDialog.Builder(it)

            builder.setView(requireActivity().layoutInflater.inflate(R.layout.layout_dialog,null))
            builder.setPositiveButton("Summit", DialogInterface.OnClickListener({ dialog, id->


            }))
            builder.create()
        }?:throw IllegalStateException("Activity is Null!!")

    }
}