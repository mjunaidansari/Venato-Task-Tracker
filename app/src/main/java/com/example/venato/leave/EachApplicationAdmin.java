package com.example.venato.leave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.venato.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class EachApplicationAdmin extends AppCompatActivity {

    private static final String TAG = "EachApplicationAdmin";

    AdminLeaveApplication leaveApplication;

    //layout views
    TextView leaveType;
    TextView startDate;
    TextView endDate;
    TextView leaveDesc;
    TextView leaveStatus;
    TextView remarkTitle;
    EditText remarkE;
    TextView remarkT;

    LinearLayout action;
    Button approve;
    Button decline;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_application_admin);

        //set back icon
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        leaveApplication = (AdminLeaveApplication) getIntent().getSerializableExtra("leave");
        Log.d(TAG, "onCreate: " + leaveApplication);

        leaveType = findViewById(R.id.leaveType);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        leaveDesc = findViewById(R.id.leaveDesc);
        leaveStatus = findViewById(R.id.leaveStatus);
        remarkTitle = findViewById(R.id.leaveRemarkTitle);
        remarkE = findViewById(R.id.leaveRemarkE);
        remarkT = findViewById(R.id.leaveRemarkT);
        action = findViewById(R.id.action);
        approve = findViewById(R.id.approve);
        decline = findViewById(R.id.decline);

        leaveType.setText(leaveApplication.getLeaveType());
        leaveDesc.setText(leaveApplication.getLeaveDesc());
        startDate.setText(leaveApplication.getStartDate());
        endDate.setText(leaveApplication.getEndDate());
        leaveStatus.setText(leaveApplication.getStringStatus());

        if(leaveApplication.getStatus() == -1) {
            remarkT.setVisibility(View.GONE);
        }
        else {
            remarkE.setVisibility(View.GONE);
            remarkT.setText(leaveApplication.getRemark());
            action.setVisibility(View.GONE);
        }

        setTitle("Leave Application Details");

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!remarkE.getText().toString().equals("")) {
                    firestore.collection("employees").document(leaveApplication.getEmpUsername()).collection("leave").document(leaveApplication.getLeaveId()).update("status", "1", "remark", remarkE.getText().toString());
                    finish();
                }
                else {
                    Toast.makeText(EachApplicationAdmin.this, "Please enter remark...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!remarkE.getText().toString().equals("")) {
                    firestore.collection("employees").document(leaveApplication.getEmpUsername()).collection("leave").document(leaveApplication.getLeaveId()).update("status", "0", "remark", remarkE.getText().toString());
                    finish();
                }
                else {
                    Toast.makeText(EachApplicationAdmin.this, "Please enter remark...", Toast.LENGTH_SHORT).show();
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