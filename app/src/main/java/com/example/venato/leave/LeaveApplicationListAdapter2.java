package com.example.venato.leave;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.venato.R;
import com.example.venato.employeetasklist.EmployeeTaskListAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaveApplicationListAdapter2  extends RecyclerView.Adapter<LeaveApplicationListAdapter2.MyViewHolder> {

    private static final String TAG = "LeaveApplicationList2";

    List<AdminLeaveApplication> applicationList;
    Context context;
    String username;

    public LeaveApplicationListAdapter2(List<AdminLeaveApplication> applicationList, Context context, String username) {
        this.applicationList = applicationList;
        this.context = context;
        this.username = username;
        Log.d(TAG, "LeaveApplicationListAdapter2: " + applicationList);
    }

    @NonNull
    @Override
    public LeaveApplicationListAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_la_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveApplicationListAdapter2.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.empName.setText(applicationList.get(position).getEmpName());
        Glide.with(this.context).load(applicationList.get(position).getImg()).into(holder.empImg);
        holder.leaveType.setText(applicationList.get(position).getLeaveType());
        holder.statusInfo.setText(applicationList.get(position).getStringStatus());
        holder.eachApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, EachApplicationAdmin.class);
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

        TextView leaveType, statusInfo, empName;
        CircleImageView empImg;
        RelativeLayout eachApplication;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            empName = itemView.findViewById(R.id.employee_name);
            empImg = itemView.findViewById(R.id.employee_image);
            leaveType = itemView.findViewById(R.id.leave_type);
            statusInfo = itemView.findViewById(R.id.leave_status);
            eachApplication = itemView.findViewById(R.id.eachApplication);
        }
    }
}
