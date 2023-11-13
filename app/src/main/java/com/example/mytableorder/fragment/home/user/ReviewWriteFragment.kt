package com.example.mytableorder.fragment.home.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase


class ReviewWriteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_review_write, container, false)

        // View를 찾아와서 처리할 작업들을 여기에 추가

        return view
    }

}