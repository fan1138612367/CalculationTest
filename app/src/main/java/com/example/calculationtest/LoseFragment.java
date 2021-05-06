package com.example.calculationtest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calculationtest.databinding.FragmentLoseBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoseFragment extends Fragment {

    public LoseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MyViewModel myViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(requireActivity().getApplication(), this)).get(MyViewModel.class);
        FragmentLoseBinding binding;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lose, container, false);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.button10.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loseFragment_to_titleFragment));
        return binding.getRoot();
    }
}