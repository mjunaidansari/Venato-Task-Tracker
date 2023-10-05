package com.example.venato.leave;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.venato.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddLeaveApplication extends BottomSheetDialogFragment {

    public static final String TAG = "AddLeaveApplication";

    Context context;

    //layout views
    private EditText leaveTypeView;
    private EditText leaveDescView;
    private TextView startDateView;
    private TextView endDateView;
    private Button addApplication;

    //application items
    private String leaveId;
    private String leaveType;
    private String leaveDesc;
    private String startDate;
    private String endDate;
    private int status;
    private String remark;

    String username;
    FirebaseFirestore firestore;

    private Map<String, String> application;

    public AddLeaveApplication(String username) {
        this.username = username;
    }

    public AddLeaveApplication(){}

    public void setUsername(String username) {
        this.username = username;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_leave_application, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        leaveTypeView = view.findViewById(R.id.leaveType);
        leaveDescView = view.findViewById(R.id.leaveDesc);
        startDateView = view.findViewById(R.id.startDate);
        endDateView = view.findViewById(R.id.endDate);
        addApplication = view.findViewById(R.id.addApplication);

        firestore = FirebaseFirestore.getInstance();

        startDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                //get current date
                int MONTH =calendar.get(Calendar.MONTH);
                int YEAR =calendar.get(Calendar.YEAR);
                int DAY =calendar.get(Calendar.DATE);

                //launch date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        startDateView.setText(dayOfMonth + "/" + month + "/" + year);
                        //set due date
                        startDate = dayOfMonth + "/" + month + "/" + year;
                    }
                }, YEAR, MONTH, DAY);

                datePickerDialog.show();

            }
        });

        endDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                //get current date
                int MONTH =calendar.get(Calendar.MONTH);
                int YEAR =calendar.get(Calendar.YEAR);
                int DAY =calendar.get(Calendar.DATE);

                //launch date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        endDateView.setText(dayOfMonth + "/" + month + "/" + year);
                        //set due date
                        endDate = dayOfMonth + "/" + month + "/" + year;
                    }
                }, YEAR, MONTH, DAY);

                datePickerDialog.show();

            }
        });

        //adding application in the database
        addApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()) {
                    try {
                        leaveType = leaveTypeView.getText().toString();
                        leaveDesc = leaveDescView.getText().toString();
                        status = -1;

                        application = new HashMap<>();

                        application.put("leaveType", leaveType);
                        application.put("leaveDesc", leaveDesc);
                        application.put("startDate", startDate);
                        application.put("endDate", endDate);
                        application.put("status", Integer.toString(status));
                        application.put("remark", "");

                        Log.d(TAG, "onClick: " + application);

                        firestore.collection("employees").document(username).collection("leave").add(application);

                        dismiss();

                    }
                    catch (Exception e) {
                        Log.e(TAG, "onClick: Error in adding the task", e);
                    }
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    //to validate all the fields of add task form
    public boolean validateFields(){
        if(leaveTypeView.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(context, "Please enter a valid leave type", Toast.LENGTH_SHORT).show();
            return false;
        } else if(leaveDescView.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(context, "Please enter valid leave description", Toast.LENGTH_SHORT).show();
            return false;
        } else if(startDateView.getText().toString().equals("")){
            Toast.makeText(context, "Please enter start date", Toast.LENGTH_SHORT).show();
            return false;
        } else if(endDateView.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(context, "Please enter end date", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}
