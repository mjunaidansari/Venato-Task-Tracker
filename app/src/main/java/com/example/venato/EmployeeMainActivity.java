package com.example.venato;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.venato.attendance.CalendarBottomSheetFragment;
import com.example.venato.attendance.GetDates;
import com.example.venato.database.DataBaseHelper;
import com.example.venato.employeetasklist.EmployeeTaskListAdapter2;
import com.example.venato.employeetasklist.Tasks;
import com.example.venato.leave.EmployeeApplications;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMainActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeMainActivity";

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

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

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    List<Tasks> tasksList = new ArrayList<>();

    DataBaseHelper db = new DataBaseHelper(this);

    String username;

    public void setUsername(Context context) {
        this.username = db.getUsername(context);
    }

    //attendance
    List<String> dates = new ArrayList<>();

    public void setDates(String username) {

        GetDates getDates = new GetDates(username);
        this.dates = getDates.getDates();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);

        setUsername(this);
        setDates(username);
        Log.d(TAG, "onCreate: " + dates.toString());

        tasksList = new ArrayList<>();

        fillTaskList();

        Log.d(TAG, "onCreateView: " + tasksList.toString());
        Toast.makeText(getApplicationContext(), "Tasks Count: " + tasksList.size(), Toast.LENGTH_SHORT).show();

        //recyclerview
        recyclerView = findViewById(R.id.employee_task_list);
        recyclerView.hasFixedSize();

        //use a linear layout manager
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        //specify an adapter
        adapter = new EmployeeTaskListAdapter2(tasksList, db.getUsername(getApplicationContext()), getApplicationContext(), getSupportFragmentManager());
        recyclerView.setAdapter(adapter);

        //set action bar title
        setTitle("Venato");

    }

    public void fillTaskList() {
        firestore.collection("employees").document(username).collection("tasks").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                tasksList.clear();

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

                    adapter.notifyDataSetChanged();

                }
                Log.d(TAG, "Tasks List: " + tasksList);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.employee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:

                AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeMainActivity.this);
                builder.setTitle("Logout")
                        .setMessage("Log out of Venato?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                db.deleteData();
                                Toast.makeText(EmployeeMainActivity.this, "Successfully Logged out", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginChoice.class);
                                startActivity(intent);
                                stopService(new Intent(getApplicationContext(), TaskService.class));
                                finish();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //do nothing
                                dialogInterface.dismiss();

                            }
                        }).show();
                return true;

            case R.id.attendance:

                CalendarBottomSheetFragment calendarBottomSheetFragment = new CalendarBottomSheetFragment();
//                GetDates getDates = new GetDates(db.getUsername(getApplicationContext()));
                calendarBottomSheetFragment.setDates(dates);
                calendarBottomSheetFragment.setUsername(db.getUsername(getApplicationContext()));
                calendarBottomSheetFragment.show(getSupportFragmentManager(), CalendarBottomSheetFragment.TAG);
                return true;

            case R.id.leaveApplications:
                Intent intent = new Intent(getApplicationContext(), EmployeeApplications.class);
                startActivity(intent);
                return true;

//            case R.id.service:
//                startService(new Intent(getApplicationContext(), TaskService.class));
//                Log.d(TAG, "onEvent: Service Started.");
//                return true;
//
//            case R.id.stopService:
//                stopService(new Intent(getApplicationContext(), TaskService.class));
//                Log.d(TAG, "onEvent: Service Stopped.");
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}