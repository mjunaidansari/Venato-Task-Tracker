package com.example.venato.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.venato.EmployeeMainActivity;
import com.example.venato.R;
import com.example.venato.ui.progress.EachProgressActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProgressGridAdapter extends ArrayAdapter<Employees> {


    public ProgressGridAdapter(@NonNull Context context,  ArrayList<Employees> employeesList) {
        super(context,0,  employeesList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.each_progress_item, parent, false);
        }
        Employees employees = getItem(position);
        CardView cardView = listItemView.findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EachProgressActivity.class);
                getContext().startActivity(intent);
            }
        });
        return listItemView;
    }
}
