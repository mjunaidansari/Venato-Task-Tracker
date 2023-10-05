package com.example.venato.ui.progress;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.venato.EachEmployeeActivity;
import com.example.venato.R;
import com.example.venato.ui.home.Employees;
import com.example.venato.ui.home.homeEmployeeListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ProgressListAdapter extends RecyclerView.Adapter<ProgressListAdapter.MyViewHolder> {

    private static final String TAG = "ProgressListAdapter";

    List<Employees> employeesList;
    Context context;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    int totalTasks = 10;

    Integer count = 0, total = 0;

    public ProgressListAdapter(List<Employees> employeesList, Context context) {
        this.employeesList = employeesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProgressListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_progress_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressListAdapter.MyViewHolder holder, int position) {
        int index = position;
        holder.empName.setText(employeesList.get(position).getName());
        Glide.with(this.context).load(employeesList.get(position).getImg()).into(holder.empImage);
        holder.taskProgress.setMax(Integer.parseInt(employeesList.get(position).getTaskCount()));
        holder.taskProgress.setProgress(Integer.parseInt(employeesList.get(position).getCompletedTasks()));
        holder.completedTasksCount.setText(employeesList.get(position).getCompletedTasks() + " out of " + employeesList.get(position).getTaskCount() + " tasks completed.");
//        holder.eachProgressItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getTaskCount(index, holder.taskProgress);
////                holder.taskProgress.setMax(total);
//                Toast.makeText(context, "Total Tasks: " + total, Toast.LENGTH_SHORT).show();
//                holder.taskProgress.setProgress(2);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return employeesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView empImage;
        TextView empName, completedTasksCount;
        RelativeLayout eachProgressItem;
        LinearProgressIndicator taskProgress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            empImage = itemView.findViewById(R.id.employee_image);
            empName = itemView.findViewById(R.id.employee_name);
            eachProgressItem = itemView.findViewById(R.id.eachProgressItem);
            completedTasksCount = itemView.findViewById(R.id.completedTasksCount);
            taskProgress = itemView.findViewById(R.id.taskProgress);
        }
    }
}
