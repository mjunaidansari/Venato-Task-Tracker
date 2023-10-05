package com.example.venato.ui.leave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.venato.databinding.FragmentJoinBinding;
import com.example.venato.databinding.FragmentLeaveBinding;

public class LeaveFragment extends Fragment {

    private FragmentLeaveBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LeaveViewModel galleryViewModel =
                new ViewModelProvider(this).get(LeaveViewModel.class);

        binding = FragmentLeaveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLeave;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}