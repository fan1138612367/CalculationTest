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

        val myViewModel by activityViewModels<MyViewModel>()
        binding.myViewModel = myViewModel
        binding.lifecycleOwner = requireActivity()

        if (savedInstanceState == null) {
            myViewModel.apply {
                currentScore.value = 0
                generator()
                inputText.value = getString(R.string.input_indicator)
                builder.value!!.setLength(0)
            }
        }

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
        val builder = myViewModel.builder.value!!
        buttonList.forEach { button ->
            button.setOnClickListener {
                if (builder.length < 2) {
                    builder.append(button.text)
                    myViewModel.inputText.value = builder.toString()
                }
            }
        }
        binding.buttonClear.setOnClickListener {
            builder.setLength(0)
            myViewModel.inputText.value = getString(R.string.input_indicator)
        }
        binding.buttonSubmit.setOnClickListener {
            if (builder.isEmpty()) {
                builder.append(-1)
            }
            if (builder.toString().toInt() == myViewModel.answer.value) {
                myViewModel.answerCorrect()
                builder.setLength(0)
                myViewModel.inputText.value = getString(R.string.answer_correct_message)
            } else {
                if (myViewModel.winFlag) {
                    findNavController().navigate(R.id.action_questionFragment_to_winFragment)
                    lifecycleScope.launch { myViewModel.save() }
                    myViewModel.winFlag = false
                } else {
                    findNavController().navigate(R.id.action_questionFragment_to_loseFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}