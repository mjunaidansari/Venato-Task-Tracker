package com.example.venato.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.venato.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    //Tab Layout
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //Tab Fragments
    EmployeesHomeFragment employeesHomeFragment = new EmployeesHomeFragment();
    ProgressesHomeFragment progressesHomeFragment = new ProgressesHomeFragment();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tabLayout = binding.tablayout;
        viewPager = binding.viewpager;

        tabLayout.setupWithViewPager(viewPager);
        
        HomeTabAdapter homeTabAdapter = new HomeTabAdapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        homeTabAdapter.addFragment(employeesHomeFragment, "Employees");
        homeTabAdapter.addFragment(progressesHomeFragment, "Progresses");

        viewPager.setAdapter(homeTabAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}