package com.example.venato.taskDialogs;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class TaskDialog3 extends DialogFragment {

    public static final String TAG = "TaskDialog3";

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

    //deadline and assigned date & time
    private String assignedOn;
    private String deadLine;

    //reference document name
    private String ref;

    //result document name
    private String res;

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskDialog3(Tasks tasks) {
        this.tasks = tasks;
    }

    public TaskDialog3() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.task_dialog3, container, false);

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
        res = tasks.getRes();

        //displaying data in the dialog
        taskName.setText(name);
        taskDetail.setText(detail);
        curDateTime.setText(assignedOn);
        dueDateTime.setText(deadLine);
        taskStatus.setText(status);
        taskPriority.setText(priority);

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
                StorageReference reference = storageReference.child("employees").child(username).child(res);
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        refDownloadUrl = uri.toString();
//                        Toast.makeText(context, refDownloadUrl, Toast.LENGTH_SHORT).show();
                        try {
                            DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//                            Uri file = Uri.parse(refDownloadUrl);
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setTitle("Result Document");
                            request.setDescription("Downloading");
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, res);
                            downloadmanager.enqueue(request);
                        } catch (Exception e) {
                            Log.e(TAG, "onSuccess: exception", e);
                        }
                        dismiss();
                    }
                });
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
