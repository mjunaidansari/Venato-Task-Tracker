package com.example.venato;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.venato.attendance.CalendarBottomSheetFragment;
import com.example.venato.attendance.GetDates;
import com.example.venato.employeetasklist.AllTasks;
import com.example.venato.employeetasklist.EmployeeTaskListAdapter;
import com.example.venato.employeetasklist.EmployeeTaskListAdapter2;
import com.example.venato.employeetasklist.Tasks;
import com.example.venato.ui.home.AllEmployees;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EachEmployeeActivity extends AppCompatActivity {

    private static final String TAG = "EachEmployeeActivity";

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

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

    List<Tasks> tasksList;

    //Task Map
    Map<String, String> task = new HashMap<>();

    String username;

    //attendance
    List<String> dates = new ArrayList<>();

    public void setDates(String username) {

        GetDates getDates = new GetDates(username);
        this.dates = getDates.getDates();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_employee);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //back icon
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent old = getIntent();
        username = old.getStringExtra("username");
        String name = old.getStringExtra("name");

        tasksList = new ArrayList<>();
        fillTaskList();
        Log.d(TAG, "onCreateView: " + tasksList.toString());
        Toast.makeText(getApplicationContext(), "Tasks Count: " + tasksList.size(), Toast.LENGTH_SHORT).show();

        //recyclerview
        recyclerView = findViewById(R.id.task_list);
        recyclerView.hasFixedSize();

        //use a linear layout manager
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        //specify an adapter
        adapter = new EmployeeTaskListAdapter(tasksList, username, getApplicationContext(), getSupportFragmentManager());
        recyclerView.setAdapter(adapter);

        //FAB
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTask addTask = new AddTask(username);
                addTask.show(getSupportFragmentManager(), AddTask.TAG);

            }
        });

        //Set action bar title to username
        setTitle(name);

        //setting dates for attendance
        setDates(username);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.each_employee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;

            case R.id.attendance:

                CalendarBottomSheetFragment calendarBottomSheetFragment = new CalendarBottomSheetFragment("admin");
//                GetDates getDates = new GetDates(db.getUsername(getApplicationContext()));
                calendarBottomSheetFragment.setDates(dates);
                calendarBottomSheetFragment.setUsername(username);
                calendarBottomSheetFragment.show(getSupportFragmentManager(), CalendarBottomSheetFragment.TAG);
                Log.d(TAG, "onOptionsItemSelected: " + dates.toString());
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void fillTaskList() {
        firestore.collection("employees").document(username).collection("tasks").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null) {
                    Log.d(TAG, "Tasks Error");
                } else {
                    tasksList.clear();
                    for(DocumentSnapshot doc: value){

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

                        adapter.notifyDataSetChanged();

                    }
                    Log.d(TAG, "Tasks List: " + tasksList);
                }
            }
        });
    }
}