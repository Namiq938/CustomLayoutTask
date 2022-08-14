package com.namig.customlayout.layouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.namig.customlayout.databinding.FragmentFirstConditionBinding

class FirstConditionFragment : Fragment() {
    lateinit var binding: FragmentFirstConditionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstConditionBinding.inflate(inflater, container, false)
        return binding.root
    }
}