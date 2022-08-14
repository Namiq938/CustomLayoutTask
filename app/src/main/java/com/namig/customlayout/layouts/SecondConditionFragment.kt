package com.namig.customlayout.layouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.namig.customlayout.databinding.FragmentSecondConditionBinding

class SecondConditionFragment : Fragment() {
    lateinit var binding: FragmentSecondConditionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondConditionBinding.inflate(inflater, container, false)
        return binding.root
    }
}