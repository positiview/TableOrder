package com.example.mytableorder.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mytableorder.R



class DetailsFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // arguments에서 position 값을 추출합니다.
        val position = arguments?.getInt("position") ?: -1
        // TODO: position 값을 사용하여 UI를 업데이트하거나 데이터를 로드합니다.
    }
}