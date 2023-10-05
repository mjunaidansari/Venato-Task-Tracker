package com.example.venato.employeetasklist;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllTasks {

    private List<Tasks> tasksList = new ArrayList<Tasks>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public static final String TAG = "AllTasks";

    //task items
    private String taskId;
    private String taskName;
    private String taskDetail;
    private String date;
    private String time;
    private String dueDate;
    private String dueTime;
    private int priority;
    private int status;

    private String ref;
    private String res;

    public AllTasks(String username) {
        fillTasksList(username);
    }

    private void fillTasksList(String username) {
        firestore.collection("employees").document(username).collection("tasks").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot doc: value){
                    if(error!=null) {
                        Log.d(TAG, "Tasks Error");
                    }

                    taskId = doc.getId();
                    taskName = doc.getString("taskName");
                    taskDetail = doc.getString("taskDetail");
                    date = doc.getString("date");
                    time = doc.getString("time");
                    dueDate = doc.getString("dueDate");
                    dueTime = doc.getString("dueTime");
                    priority = Integer.parseInt(doc.getString("priority"));
                    status = Integer.parseInt(doc.getString("status"));
                    ref = doc.getString("ref");
                    res = doc.getString("res");

                    Tasks task = new Tasks(taskId, taskName, taskDetail, date, time , dueDate, dueTime, priority, status, ref, res);

                    tasksList.add(task);

                }
                Log.d(TAG, "Tasks List: " + tasksList);
            }
        });
    }

    public List<Tasks> getTasksList() {
        return tasksList;
    }

    public void setTasksList(List<Tasks> tasksList) {
        this.tasksList = tasksList;
    }

}
