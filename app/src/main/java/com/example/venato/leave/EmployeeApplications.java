package com.example.venato.leave;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.venato.R;
import com.example.venato.database.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EmployeeApplications extends AppCompatActivity {

    private static final String TAG = "EmployeeApplications";

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    DataBaseHelper db = new DataBaseHelper(this);

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    //LeaveApplication items
    private String leaveId;
    private String leaveType;
    private String leaveDesc;
    private String startDate;
    private String endDate;
    private int status;
    private String remark;

    List<LeaveApplication> applicationList;

    String username;

    public void setUsername (Context context){
        this.username = db.getUsername(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_applications);

        //back icon
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setUsername(this);

        applicationList = new ArrayList<>();

        fillApplicationList();

        recyclerView = findViewById(R.id.applicationList);
        recyclerView.hasFixedSize();

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new LeaveApplicationListAdapter1(applicationList, getApplicationContext(), username);
        recyclerView.setAdapter(adapter);

        setTitle("My Leave Applications");

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddLeaveApplication addLeaveApplication = new AddLeaveApplication(username);
                addLeaveApplication.show(getSupportFragmentManager(), AddLeaveApplication.TAG);
            }
        });

    }

    private void fillApplicationList() {
        firestore.collection("employees")
                .document(username)
                .collection("leave")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error!=null) {
                            Log.d(TAG, "onEvent: Data Fetching Error");
                        }
                        else {

                            for (DocumentSnapshot doc: value) {

                                Log.d(TAG, "onEvent: iteration");
                                leaveId = doc.getId();
                                leaveType = doc.getString("leaveType");
                                leaveDesc = doc.getString("leaveDesc");
                                startDate = doc.getString("startDate");
                                endDate = doc.getString("endDate");
                                remark = doc.getString("remark");
                                status  = Integer.parseInt(doc.getString("status"));

                                LeaveApplication leaveApplication = new LeaveApplication(leaveId, leaveType, leaveDesc, startDate, endDate, status, remark);

                                applicationList.add(leaveApplication);

                                adapter.notifyDataSetChanged();

                            }
                            Log.d(TAG, "onEvent: leave application list: " + applicationList);
                        }
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }
}