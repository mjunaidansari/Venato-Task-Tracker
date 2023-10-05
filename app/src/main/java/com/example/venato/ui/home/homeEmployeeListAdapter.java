package com.example.venato.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.venato.EachEmployeeActivity;
import com.example.venato.EmployeeMainActivity;
import com.example.venato.R;

import java.util.List;

public class homeEmployeeListAdapter extends RecyclerView.Adapter<homeEmployeeListAdapter.MyViewHolder> {

    List<Employees> employeesList;
    Context context;

    public homeEmployeeListAdapter(List<Employees> employeesList, Context context) {
        this.employeesList = employeesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_employee_list, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int index = position;
        holder.empName.setText(employeesList.get(position).getName());
        Glide.with(this.context).load(employeesList.get(position).getImg()).into(holder.empImage);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EachEmployeeActivity.class);
                intent.putExtra("username", employeesList.get(position).getUsername());
                intent.putExtra("name", employeesList.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView empImage;
        TextView empName;
        RelativeLayout relativeLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            empImage = itemView.findViewById(R.id.employee_image);
            empName = itemView.findViewById(R.id.employee_name);
            relativeLayout = itemView.findViewById(R.id.homeEmployeeList);
        }
    }
}
