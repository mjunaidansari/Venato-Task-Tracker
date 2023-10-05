package com.example.venato;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.venato.database.DataBaseHelper;
import com.example.venato.employeetasklist.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class EmployeeLoginActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeLoginActivity";

    private TextInputEditText username, password;
    private TextInputLayout usernamet, passwordt;
    private Button login, forgot;
    DataBaseHelper db = new DataBaseHelper(this);
    private FirebaseFirestore firestore;

    @Override
    protected void onStart() {
        super.onStart();

//        if(!db.isEmpty()) {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);
        getSupportActionBar().hide();

        username = findViewById(R.id.username);
        usernamet = findViewById(R.id.usernamet);
        password = findViewById(R.id.password);
        passwordt = findViewById(R.id.passwordt);
        login = findViewById(R.id.login);
        forgot = findViewById(R.id.forgotpassword);

        firestore = FirebaseFirestore.getInstance();

        //false until user add values in edit text
        login.setEnabled(false);

        //adding listeners on edit texts
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = username.getText().toString();
                String pass = password.getText().toString();
                if(pass.equals("") || name.equals("")){
                    login.setEnabled(false);
                } else {
                    login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = username.getText().toString();
                String pass = password.getText().toString();
                if(pass.equals("") || name.equals("")){
                    login.setEnabled(false);
                } else {
                    login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = username.getText().toString();
                String pass = password.getText().toString();

                //searching for the username
                firestore.collection("employees")
                        .whereEqualTo("username", name)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error!=null || value.size()==0) {
//                                    Toast.makeText(AdminLoginActivity.this, "Admin not Found", Toast.LENGTH_SHORT).show();
                                    usernamet.setError("Employee Not Found");
                                }
                                for (DocumentSnapshot doc : value) {
                                    String pass1 = doc.getString("password");
                                    if(pass.equals(pass1)) {
                                        usernamet.setError(null);
                                        passwordt.setError(null);
                                        insert(name, pass);
                                        Intent intent = new Intent(getApplicationContext(), EmployeeMainActivity.class);
                                        startActivity(intent);
                                        finish();
//                                        startService(new Intent(getApplicationContext(), TaskService.class));
//                                        Log.d(TAG, "onEvent: Service Started.");
                                    }
                                    else {
//                                        Toast.makeText(AdminLoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                                        usernamet.setError(null);
                                        passwordt.setError("Invalid Password");
                                    }
                                }
                            }
                        });

            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EmployeeLoginActivity.this,"Contact Administrator",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void insert(String username, String password) {
        boolean inserted = db.insertData(username, password, "E");
        if(inserted) {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginChoice.class);
        startActivity(intent);
        finish();
    }
}