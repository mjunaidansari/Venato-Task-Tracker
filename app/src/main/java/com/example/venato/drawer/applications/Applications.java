package com.example.venato.drawer.applications;

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
import android.widget.Toast;

import com.example.venato.R;
import com.example.venato.database.DataBaseHelper;
import com.example.venato.leave.AdminLeaveApplication;
import com.example.venato.leave.LeaveApplication;
import com.example.venato.leave.LeaveApplicationListAdapter1;
import com.example.venato.leave.LeaveApplicationListAdapter2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Applications extends AppCompatActivity {

    private static final String TAG = "Applications";

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    DataBaseHelper db = new DataBaseHelper(this);

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    //LeaveApplication items
    private String leaveId;
    private String leaveType;
    private String leaveDesc;
    private String startDate;
    private String endDate;
    private int status;
    private String remark;
    private String empUsername;
    private String empName;
    private String empImg;

    List<AdminLeaveApplication> applicationList;

    String username;

    public void setUsername (Context context){
        this.username = db.getUsername(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);

        //set back icon
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setUsername(this);

        applicationList = new ArrayList<>();

        fillApplicationList();

        recyclerView = findViewById(R.id.adminApplicationList);
        recyclerView.hasFixedSize();

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new LeaveApplicationListAdapter2(applicationList, getApplicationContext(), username);
        recyclerView.setAdapter(adapter);

        //set title of the activity
        setTitle("Leave Applications");

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        Log.d(TAG, "onResume: onResume called");
//
//    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(TAG, "onRestart: onRestart called");
        fillApplicationList();

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

    public void fillApplicationList() {
        firestore.collection("employees").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error!=null) {
                    Log.d(TAG, "Error in data fetching");
                    Toast.makeText(Applications.this, "Error in data fetching", Toast.LENGTH_SHORT).show();
                }
                else {
                    applicationList.clear();
                    for(DocumentSnapshot emp: value) {
                        firestore.collection("employees")
                                .document(emp.getId())
                                .collection("leave")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                        for(DocumentChange documentChange: value.getDocumentChanges()) {
                                            Log.d(TAG, "onEvent: document changed " + documentChange.getDocument().getData());
                                        }

                                        if (error != null) {
                                            Log.d(TAG, "onEvent: Data Fetching Error");
                                        } else {
                                            for (DocumentSnapshot application : value) {
//                                                Log.d(TAG, "onEvent: iteration");

                                                leaveId = application.getId();
                                                leaveType = application.getString("leaveType");
                                                leaveDesc = application.getString("leaveDesc");
                                                startDate = application.getString("startDate");
                                                endDate = application.getString("endDate");
                                                remark = application.getString("remark");
                                                status = Integer.parseInt(application.getString("status"));
                                                empUsername = emp.getString("username");
                                                empName = emp.getString("name");
                                                empImg = emp.getString("img");

                                                AdminLeaveApplication adminLeaveApplication = new AdminLeaveApplication(leaveId, leaveType, leaveDesc, startDate, endDate, status, remark, empUsername, empName, empImg);

                                                Log.d(TAG, "onEvent: applicant: " + emp.getString("name"));
                                                applicationList.add(adminLeaveApplication);

                                                adapter.notifyDataSetChanged();

                                            }
                                            Log.d(TAG, "onEvent: leave application list: " + applicationList);
                                        }
                                    }
                                });
                    }
                }

            }
        });
    }
}