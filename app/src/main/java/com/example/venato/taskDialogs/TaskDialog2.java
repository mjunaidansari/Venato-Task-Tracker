package com.example.venato.taskDialogs;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.venato.AddTask;
import com.example.venato.R;
import com.example.venato.employeetasklist.AllTasks;
import com.example.venato.employeetasklist.Tasks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class TaskDialog2 extends DialogFragment {

    public static final String TAG = "TaskDialog2";
    private static final int REQUEST_CODE = 2;

    Context context;

    //layout views
    private TextView taskName;
    private TextView taskDetail;
    private TextView curDateTime;
    private TextView dueDateTime;
    private TextView taskPriority;
    private TextView taskStatus;
    private Button complete;

    private TextView refDoc;
    private TextView resDoc;

    //task items
    private String taskId;
    private String name;
    private String detail;
    private String curDate;
    private String curTime;
    private String dueDate;
    private String dueTime;
    private String priority;
    private String status;

    //reference document name
    private String ref;

    //result document name
    private String res;

    //deadline and assigned date & time
    private String assignedOn;
    private String deadLine;

    //for getting the data
    private String username;
    private List<Tasks> tasksList;
    Tasks tasks;
    AllTasks allTasks;

    FirebaseFirestore firestore;
    StorageReference storageReference;
    String refDownloadUrl;
    String resDownloadUrl;

    //adding the result document
    Uri file;

    FragmentManager fragmentManager;

    //number of tasks completed
    int completedTasks;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskDialog2(Tasks tasks, FragmentManager fragmentManager) {
        this.tasks = tasks;
        this.fragmentManager = fragmentManager;
    }

    public TaskDialog2() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.task_dialog2, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        taskName = view.findViewById(R.id.taskName);
        taskDetail = view.findViewById(R.id.taskDetail);
        curDateTime = view.findViewById(R.id.curDateTime);
        dueDateTime = view.findViewById(R.id.dueDateTime);
        taskPriority = view.findViewById(R.id.taskPriority);
        taskStatus = view.findViewById(R.id.taskStatus);
        complete = view.findViewById(R.id.complete);

        refDoc = view.findViewById(R.id.refDoc);
        resDoc = view.findViewById(R.id.resDoc);

        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //getting data from current task object
        name = tasks.getTaskName();
        detail = tasks.getTaskDetail();
        curDate = tasks.getDate();
        dueDate = tasks.getDueDate();
        curTime = tasks.getTime();
        dueTime = tasks.getDueTime();
        status = tasks.returnStatus();
        priority = tasks.returnPriority();
        assignedOn = tasks.returnAssignedOn();
        deadLine = tasks.returnDeadline();

        ref = tasks.getRef();

        //displaying data in the dialog
        taskName.setText(name);
        taskDetail.setText(detail);
        curDateTime.setText(assignedOn);
        dueDateTime.setText(deadLine);
        taskStatus.setText(status);
        taskPriority.setText(priority);

        //complete button should be disable before uploading the document
        complete.setEnabled(false);
        complete.setBackgroundResource(R.drawable.btn_round_corners_d);

        //on click of mark as complete button
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Completed the Task!..")
                        .setMessage("Are you sure you want to mark this task as completed.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {



                                //incrementing the number of tasks
                                completedTasks = 0;
                                firestore.collection("employees")
                                        .document(username)
                                        .collection("tasks")
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for(DocumentSnapshot doc: task.getResult()) {
                                                if(doc.getString("status").equals("1")){
                                                    completedTasks++;
                                                }
                                            }
                                            Log.d(TAG, "onComplete: number of tasks completed" + completedTasks);
                                            firestore.collection("employees").document(username).update("completedTasks", Integer.toString(completedTasks + 1));
                                        }
                                    }
                                });

                                firestore.collection("employees").document(username).collection("tasks").document(tasks.getTaskId()).update("status", "1");
                                firestore.collection("employees").document(username).collection("tasks").document(tasks.getTaskId()).update("res", res);
                                uploadDoc(file);
                                Toast.makeText(context, "Task Completed", Toast.LENGTH_SHORT).show();
                                dismiss();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //do nothing
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });

        refDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference reference = storageReference.child("employees").child(username).child(ref);
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        refDownloadUrl = uri.toString();
                        try {
                            DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//                            Uri file = Uri.parse(refDownloadUrl);
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setTitle("Reference Document");
                            request.setDescription("Downloading");
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ref);
                            downloadmanager.enqueue(request);
                        } catch (Exception e) {
                            Log.e(TAG, "onSuccess: exception", e);
                        }
                        dismiss();
                    }
                });
            }
        });

        resDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDoc();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE && resultCode== Activity.RESULT_OK && data!=null && data.getData()!=null) {
            file = data.getData();
            res = getFileName(file);
            resDoc.setText(res);
            complete.setEnabled(true);
            complete.setBackgroundResource(R.drawable.btn_round_corners);
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


    private void uploadDoc(Uri file) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading Document...");
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
}
