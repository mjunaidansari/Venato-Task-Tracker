package com.example.venato;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SafeBrowsingResponse;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddTask";

    public static final int REQUEST_CODE = 12;

    Context context;

    //layout views
    private TextView title;
    private EditText taskName;
    private EditText taskDetail;
    private TextView taskDate;
    private TextView taskTime;
    private Spinner taskPriority;
    private Button addTask;
    private TextView doc;
//    private Spinner docType;

    public AddTask(String username) {
        this.username = username;
    }

    //task map
    private Map<String, String> task;

    //task items
    private String taskId;
    private String name;
    private String detail;
    private String curDate;
    private String curTime;
    private String dueDate;
    private String dueTime;
    private int priority;
    private int status;

    //for adding the data
    private String username;
    FirebaseFirestore firestore;

    //updating the task
    boolean isUpdate;
    private Bundle bundle;

    //adding the reference document
    Uri file;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    String[] priorities = new String[]{"Medium", "High", "Low"};

    //number of tasks of the employee
    int taskCount;

    public AddTask() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_task, container, false);

    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        taskName = view.findViewById(R.id.taskName);
        taskDetail = view.findViewById(R.id.taskDetail);
        taskDate = view.findViewById(R.id.taskDate);
        taskTime = view.findViewById(R.id.taskTime);
        taskPriority = view.findViewById(R.id.taskPriority);
        addTask = view.findViewById(R.id.addTask);
        title = view.findViewById(R.id.title);
        doc = view.findViewById(R.id.doc);
//        docType = view.findViewById(R.id.docType);

        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Disable add task button till any data is entered
        addTask.setEnabled(false);
        addTask.setBackgroundResource(R.drawable.btn_round_corners_d);

        //set adapter for priority dropdown
        final List<String> priorityList = new ArrayList<>(Arrays.asList(priorities));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                priorityList);
        taskPriority.setAdapter(adapter1);

        isUpdate = false;

        //getting the data if it is an update action
        bundle = getArguments();
        if(bundle != null) {

            isUpdate = true;
            title.setText("Update a Task");
            addTask.setText("Update");

            //getting values from the bundle
            taskId = bundle.getString("taskId");
            name = bundle.getString("taskName");
            detail = bundle.getString("taskDetail");
            dueDate = bundle.getString("taskDate");
            dueTime = bundle.getString("taskTime");
            priority = bundle.getInt("priority");
            status = bundle.getInt("status");

            //setting the values in layout views
            taskName.setText(name);
            taskDetail.setText(detail);
            taskDate.setText(dueDate);
            taskTime.setText(dueTime);
            taskPriority.setSelection(setPriority());

            //button should be enabled on update dialog
            addTask.setEnabled(true);
            addTask.setBackgroundResource(R.drawable.btn_round_corners);
//
        }

        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                //get current date
                int MONTH =calendar.get(Calendar.MONTH);
                int YEAR =calendar.get(Calendar.YEAR);
                int DAY =calendar.get(Calendar.DATE);

                //set current date
                curDate = DAY + "/" + (MONTH + 1) + "/" + YEAR;

                //launch date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        taskDate.setText(dayOfMonth + "/" + month + "/" + year);
                        //set due date
                        dueDate = dayOfMonth + "/" + month + "/" + year;
                    }
                }, YEAR, MONTH, DAY);

                datePickerDialog.show();

            }
        });

        taskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                //get current time
                int HOUR = calendar.get(Calendar.HOUR_OF_DAY);
                int MINUTE = calendar.get(Calendar.MINUTE);

                //set current time
                curTime = HOUR + ":" + MINUTE;

                //launch time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        taskTime.setText(hourOfDay + ":" + minute);
                        dueTime = hourOfDay + ":" + minute;
                    }
                }, HOUR, MINUTE, false);

                timePickerDialog.show();

            }
        });

        priority = 1;

        //set priority from spinner
        taskPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0) {
                    priority = 1;
                }
                else if (position == 1) {
                    priority = 2;
                }
                else {
                    priority = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//                priority = 2;
            }
        });


        //Adding values to the database
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = taskName.getText().toString();
                detail = taskDetail.getText().toString();
                status = 0;

                if (isUpdate) {

                    firestore.collection("employees").document(username).collection("tasks").document(taskId).update("taskName", name, "taskDetail", detail, "dueDate", dueDate, "dueTime", dueTime, "priority", Integer.toString(priority));

                    Toast.makeText(context, "Task Updated", Toast.LENGTH_SHORT).show();

                    dismiss();
                }
                else {
                    if(validateFields()) {
                        try{
                            task = new HashMap<>();

                            task.put("taskName", name);
                            task.put("taskDetail", detail);
                            task.put("date", curDate);
                            task.put("time", curTime);
                            task.put("dueDate", dueDate);
                            task.put("dueTime", dueTime);
                            task.put("priority", Integer.toString(priority));
                            task.put("status",Integer.toString(status));

                            task.put("ref", getFileName(file));
                            task.put("res", "");

                            Log.d(TAG, "onClick: " + task);

                            firestore.collection("employees").document(username).collection("tasks").add(task);

                            uploadDoc(file);

                            //updating  the number of tasks
                            firestore.collection("employees")
                                    .document(username)
                                    .collection("tasks")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()) {
                                        taskCount = task.getResult().size();
                                        Log.d(TAG, "onComplete: inside onevent " + taskCount);
                                        firestore.collection("employees").document(username).update("taskCount", Integer.toString(taskCount));
                                    }
                                    else {
                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            Log.d(TAG, "onClick: completed");

                            //close the fragment after button click
                            dismiss();
                        } catch(Exception e) {
                            Log.e(TAG, "onClick: Error in adding the task", e);
                        }
                    }
                }
            }
        });

        //getting file from local storage
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDoc();
            }
        });

    }

    private int setPriority() {

        if(priority==0) {
            return 2;
        }
        else if(priority==1){
            return 0;
        }
        else {
            return 1;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    //to validate all the fields of add task form
    public boolean validateFields(){
        if(taskName.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(context, "Please enter a valid task name", Toast.LENGTH_SHORT).show();
            return false;
        } else if(taskDetail.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(context, "Please enter valid task description", Toast.LENGTH_SHORT).show();
            return false;
        } else if(doc.getText().toString().equals("")){
            Toast.makeText(context, "Please select reference document", Toast.LENGTH_SHORT).show();
            return false;
        } else if(taskDate.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(context, "Please enter date", Toast.LENGTH_SHORT).show();
            return false;
        } else if(taskTime.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(context, "Please enter time", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void selectDoc() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimetypes = {"image/*", "application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", "text/plain"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), REQUEST_CODE);
//        Toast.makeText(context, type, Toast.LENGTH_SHORT).show();

    }

    private void uploadDoc(Uri file) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading Reference Document...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StorageReference reference = storageReference.child("employees").child(username).child(getFileName(file));

        reference.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setMessage((int)progress + "% uploaded...");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE && resultCode== Activity.RESULT_OK && data!=null && data.getData()!=null) {
            file = data.getData();
            doc.setText(getFileName(file));
            addTask.setEnabled(true);
            addTask.setBackgroundResource(R.drawable.btn_round_corners);
        }
    }
    private String getFileName(Uri uri) {

        Cursor mCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        int indexedname = mCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        mCursor.moveToFirst();
        String filename = mCursor.getString(indexedname);
        filename = filename.replaceAll("\\s+", "");
        mCursor.close();
        return filename;
    }
}
