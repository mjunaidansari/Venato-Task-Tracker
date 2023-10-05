package com.example.venato.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.venato.AddEmployee;
import com.example.venato.MyApplication;
import com.example.venato.R;
import com.example.venato.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.index.FirestoreIndexValueWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeesHomeFragment extends Fragment {

    private static final String TAG = "EmployeesHomeFragment";

    List<Employees> employeesList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //Floating Action Button FAB
    FloatingActionButton fab;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public EmployeesHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_employees_home, container, false);
//        View view = getView();
        assert view != null;

        employeesList = new ArrayList<>();

        fillEmployeesList();

        Log.d(TAG, "onCreateView: " + employeesList.toString());
        Toast.makeText(getActivity(), "Employees Count: "+ employeesList.size(), Toast.LENGTH_SHORT).show();

        //RecyclerView
        recyclerView = view.findViewById(R.id.employees_list);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //specify an adapter
        mAdapter = new homeEmployeeListAdapter(employeesList, getActivity());
        recyclerView.setAdapter(mAdapter);

        //Fab
        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddEmployee addEmployee = new AddEmployee();
                addEmployee.show(getParentFragmentManager(), AddEmployee.TAG);

            }
        });


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_employees_home, container, false);
        return view;
    }

    public void fillEmployeesList() {
        firestore.collection("employees").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error!=null) {
                    Log.d(TAG, "Employees error" + error.getMessage());
                }
                else {
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