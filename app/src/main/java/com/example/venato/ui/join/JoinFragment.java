package com.example.venato.ui.join;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.venato.databinding.FragmentJoinBinding;

public class JoinFragment extends Fragment {

    private FragmentJoinBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        JoinViewModel galleryViewModel =
                new ViewModelProvider(this).get(JoinViewModel.class);

        binding = FragmentJoinBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textJoin;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}