package com.example.venato.leave;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.venato.R;
import com.example.venato.employeetasklist.EmployeeTaskListAdapter;

import java.util.List;

public class LeaveApplicationListAdapter1  extends RecyclerView.Adapter<LeaveApplicationListAdapter1.MyViewHolder> {

    private static final String TAG = "LeaveApplicationList1";

    List<LeaveApplication> applicationList;
    Context context;
    String username;

    public LeaveApplicationListAdapter1(List<LeaveApplication> applicationList, Context context, String username) {
        this.applicationList = applicationList;
        this.context = context;
        this.username = username;
    }

    @NonNull
    @Override
    public LeaveApplicationListAdapter1.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_la_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveApplicationListAdapter1.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.leaveType.setText(applicationList.get(position).getLeaveType());
        holder.statusInfo.setText(applicationList.get(position).getStringStatus());
        holder.eachApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, EachApplicationEmployee.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("leave", applicationList.get(position));
                    context.startActivity(intent);
                }
                catch (Exception e) {
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView leaveType, statusInfo;
        ConstraintLayout eachApplication;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            leaveType = itemView.findViewById(R.id.leaveType);
            statusInfo = itemView.findViewById(R.id.statusInfo);
            eachApplication = itemView.findViewById(R.id.eachApplication);
        }
    }
}
