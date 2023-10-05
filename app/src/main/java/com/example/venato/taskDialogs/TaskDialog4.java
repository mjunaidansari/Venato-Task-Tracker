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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class TaskDialog4 extends DialogFragment {

    public static final String TAG = "TaskDialog4";

    Context context;

    //layout views
    private TextView taskName;
    private TextView taskDetail;
    private TextView curDateTime;
    private TextView dueDateTime;
    private TextView taskPriority;
    private TextView taskStatus;
    private Button delete;

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

    //adding the resul

    FragmentManager fragmentManager;

    //count the number of tasks
    int taskCount;

    //count the number of completed tasks
    int completedTasks;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskDialog4(Tasks tasks, FragmentManager fragmentManager) {
        this.tasks = tasks;
        this.fragmentManager = fragmentManager;
    }

    public TaskDialog4() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.task_dialog4, container, false);

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
        delete = view.findViewById(R.id.delete);

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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete the task")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //delete the task from firestore
                                firestore.collection("employees")
                                        .document(username)
                                        .collection("tasks")
                                        .document(tasks.getTaskId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getActivity(), "Task Deleted", Toast.LENGTH_SHORT).show();

                                                //updating the number of tasks
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

                                                //updating the number of completed tasks
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
                                                            firestore.collection("employees").document(username).update("completedTasks", Integer.toString(completedTasks));
                                                        }
                                                    }
                                                });

                                                dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), "Error Deleting Task", Toast.LENGTH_SHORT).show();
                                            }
                                        });
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
