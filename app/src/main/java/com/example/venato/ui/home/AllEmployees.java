package com.example.venato.ui.home;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.venato.MainActivity;
import com.example.venato.MyApplication;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AllEmployees {

    private List<Employees> employeesList= new ArrayList<Employees>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public static String TAG = "AllEmployees Class";

    public AllEmployees(){
        fillEmployeesList();
    }

    public void fillEmployeesList() {
        firestore.collection("employees").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot doc: value) {
                    if(error!=null) {
                        Log.d(TAG, "Employees error");
                    }
                    String username = doc.getString("username");
                    String name = doc.getString("name");
                    String img = doc.getString("img");
                    String taskCount = doc.getString("taskCount");
                    String completedTasks = doc.getString("completedTasks");
                    Employees e = new Employees(username, name, img, taskCount, completedTasks);
                    employeesList.add(e);
                }
                Log.d(TAG, "Employee List: " + employeesList);
            }
        });
    }
    public List<Employees> getEmployeesList() {
        return this.employeesList;
    }

    public void setEmployeesList(List<Employees> employeesList) {
        this.employeesList = employeesList;
    }
}
