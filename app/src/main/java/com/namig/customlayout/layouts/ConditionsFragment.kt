package com.namig.customlayout.layouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.namig.customlayout.R
import com.namig.customlayout.databinding.FragmentConditionsBinding

class ConditionsFragment : Fragment() {
    lateinit var binding: FragmentConditionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConditionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.firstConditionFragment.setOnClickListener {
            findNavController().navigate(R.id.action_conditionsFragment_to_firstConditionFragment)
        }
        binding.secondConditionFragment.setOnClickListener {
            findNavController().navigate(R.id.action_conditionsFragment_to_secondConditionFragment)
        }
        binding.thirdConditionFragment.setOnClickListener {
            findNavController().navigate(R.id.action_conditionsFragment_to_thirdConditionFragment2)
        }
        binding.fourthConditionFragment.setOnClickListener {
            findNavController().navigate(R.id.action_conditionsFragment_to_fourthFragment)
        }
    }


}