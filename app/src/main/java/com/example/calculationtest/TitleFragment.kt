package com.example.calculationtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.calculationtest.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {
    private var _binding: FragmentTitleBinding? = null
    private val binding get() = _binding!!
    private val myViewModel by navGraphViewModels<MyViewModel>(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myViewModel = myViewModel
        binding.lifecycleOwner = requireActivity()
        binding.button.setOnClickListener {
            it.findNavController().navigate(R.id.action_titleFragment_to_questionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}