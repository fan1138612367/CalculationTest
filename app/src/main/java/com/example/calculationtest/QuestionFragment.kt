package com.example.calculationtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.calculationtest.databinding.FragmentQuestionBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class QuestionFragment : Fragment() {
    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    private val myViewModel by activityViewModels<MyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.dialog_quit_title)
                .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                    findNavController().navigateUp()
                }
                .setNegativeButton(R.string.dialog_negative_message) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }
        if (savedInstanceState == null) {
            myViewModel.generator()
            myViewModel.currentScore.postValue(0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myViewModel = myViewModel
        binding.lifecycleOwner = requireActivity()

        val buttonList = listOf(
            binding.button0,
            binding.button1,
            binding.button2,
            binding.button3,
            binding.button4,
            binding.button5,
            binding.button6,
            binding.button7,
            binding.button8,
            binding.button9
        )
        buttonList.forEach { button ->
            button.setOnClickListener {
                myViewModel.inputText(button.text)
            }
        }
        binding.buttonSubmit.setOnClickListener {
            if (myViewModel.inputText.value!!.length <= 2) {
                if (myViewModel.inputText.value!!.toInt() == myViewModel.answer.value) {
                    myViewModel.answerCorrect()
                } else {
                    if (myViewModel.winFlag) {
                        findNavController().navigate(R.id.action_questionFragment_to_winFragment)
                        viewLifecycleOwner.lifecycleScope.launch { myViewModel.save() }
                        myViewModel.winFlag = false
                    } else {
                        findNavController().navigate(R.id.action_questionFragment_to_loseFragment)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}