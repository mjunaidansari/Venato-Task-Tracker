package com.example.venato.leave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.venato.R;

public class EachApplicationEmployee extends AppCompatActivity {

    private static final String TAG = "EachApplicationEmployee";

    LeaveApplication leaveApplication;

    //layout views
    TextView leaveType;
    TextView startDate;
    TextView endDate;
    TextView leaveDesc;
    TextView leaveStatus;
    TextView remarkTitle;
    TextView remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_application_employee);

        //set back icon
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        leaveApplication = (LeaveApplication) getIntent().getSerializableExtra("leave");
        Log.d(TAG, "onCreate: " + leaveApplication);

        leaveType = findViewById(R.id.leaveType);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        leaveDesc = findViewById(R.id.leaveDesc);
        leaveStatus = findViewById(R.id.leaveStatus);
        remarkTitle = findViewById(R.id.leaveRemarkTitle);
        remark = findViewById(R.id.leaveRemark);

        leaveType.setText(leaveApplication.getLeaveType());
        leaveDesc.setText(leaveApplication.getLeaveDesc());
        startDate.setText(leaveApplication.getStartDate());
        endDate.setText(leaveApplication.getEndDate());
        leaveStatus.setText(leaveApplication.getStringStatus());

        if(leaveApplication.getStatus() == -1) {
            remark.setVisibility(View.GONE);
            remarkTitle.setVisibility(View.GONE);
        }
        else {
            remark.setText(leaveApplication.getRemark());
        }

        setTitle("Leave Application Details");
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