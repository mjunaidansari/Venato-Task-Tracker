package com.example.venato.ui.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.venato.R;
import com.example.venato.ui.progress.ProgressListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ProgressesHomeFragment extends Fragment {

    private static final String TAG = "ProgressesHomeFragment";

    List<Employees> employeesList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public ProgressesHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progresses_home, container, false);
        assert view != null;

        employeesList = new ArrayList<>();

        fillEmployeesList();

        //RecyclerView
        recyclerView = view.findViewById(R.id.employees_list);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //specify an adapter
        mAdapter = new ProgressListAdapter(employeesList, getActivity());
        recyclerView.setAdapter(mAdapter);

        return view;

    }

    public void fillEmployeesList() {
        firestore.collection("employees").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null) {
                    Log.d(TAG, "Employees error" + error.getMessage());
                } else {
                    employeesList.clear();
                    for(DocumentSnapshot doc: value) {
                        String username = doc.getString("username");
                        String name = doc.getString("name");
                        String img = doc.getString("img");
                        String taskCount = doc.getString("taskCount");
                        String completedTasks = doc.getString("completedTasks");
                        Employees e = new Employees(username, name, img, taskCount, completedTasks);
                        employeesList.add(e);
                        mAdapter.notifyDataSetChanged();
                    }
                    Log.d(TAG, "Employee List: " + employeesList);
                }
            }
        });
    }
}