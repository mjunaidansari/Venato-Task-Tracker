    package com.example.venato.employeetasklist;

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
    import androidx.fragment.app.FragmentManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.venato.R;
    import com.example.venato.taskDialogs.TaskDialog1;
    import com.example.venato.taskDialogs.TaskDialog4;

    import java.util.List;

    public class EmployeeTaskListAdapter extends RecyclerView.Adapter<EmployeeTaskListAdapter.MyViewHolder> {

        List<Tasks> tasksList;
        Context context;
        String username;
        FragmentManager fragmentManager;
        int index;

        public void setUsername(String username) {
            this.username = username;
        }


        public EmployeeTaskListAdapter(List<Tasks> tasksList, String username, Context context, FragmentManager fragmentManager) {
            this.tasksList = tasksList;
            this.context = context;
            this.username = username;
            this.fragmentManager = fragmentManager;
        }

        @NonNull
        @Override
        public EmployeeTaskListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_task, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull EmployeeTaskListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.taskName.setText(tasksList.get(position).getTaskName());
            holder.taskDetail.setText(tasksList.get(position).getTaskDetail());
            holder.date.setText(tasksList.get(position).getDate());
            holder.priority.setText("Priority: " + tasksList.get(position).returnPriority());
            holder.status.setText("Status: " + tasksList.get(position).returnStatus());

            int s = tasksList.get(position).getStatus();

            holder.eachTaskLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(s==0) {
                        TaskDialog1 taskDialog1 = new TaskDialog1(tasksList.get(position), fragmentManager);
                        taskDialog1.setUsername(username);
                        taskDialog1.show(fragmentManager, TaskDialog1.TAG);
                    }
                    else {
                        TaskDialog4 taskDialog4 = new TaskDialog4(tasksList.get(position), fragmentManager);
                        taskDialog4.setUsername(username);
                        taskDialog4.show(fragmentManager, TaskDialog1.TAG);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return tasksList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView taskName, taskDetail, date, status, priority;
            ConstraintLayout eachTaskLayout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                taskName = itemView.findViewById(R.id.taskName);
                taskDetail = itemView.findViewById(R.id.taskDetail);
                date = itemView.findViewById(R.id.date);
                priority = itemView.findViewById(R.id.priority);
                status = itemView.findViewById(R.id.status);
                eachTaskLayout = itemView.findViewById(R.id.eachTaskLayout);

            }
        }
    }
