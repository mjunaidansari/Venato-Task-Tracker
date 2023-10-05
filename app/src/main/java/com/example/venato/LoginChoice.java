package com.example.venato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.venato.database.DataBaseHelper;

public class LoginChoice extends AppCompatActivity {

    Button admin, employee;

    DataBaseHelper db = new DataBaseHelper(this);

    @Override
    protected void onStart() {
        super.onStart();

        if(!db.isEmpty()) {

            String login = db.getLogin(getApplicationContext());

            if (login.equals("A")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
           else if(login.equals("E")){
                Intent intent = new Intent(getApplicationContext(), EmployeeMainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this, "Error in data retrieval", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);
        getSupportActionBar().hide();

        admin = findViewById(R.id.admin);
        employee = findViewById(R.id.employee);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EmployeeLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}