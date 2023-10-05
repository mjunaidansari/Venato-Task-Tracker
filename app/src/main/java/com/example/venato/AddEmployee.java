package com.example.venato;

import android.content.Context;
import android.databinding.tool.util.StringUtils;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.abego.treelayout.internal.util.java.lang.string.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class AddEmployee extends BottomSheetDialogFragment {

    public static final String TAG = "AddEmployee";

    Context context;

    //layout views
    private TextInputEditText name, username, password, cpassword;
    private TextInputLayout namet, usernamet, passwordt, cpasswordt;
    private Button add;

    //employee info
    private String empName;
    private String empUsername;
    private String empPassword;
    private String empImg;

    //firestore reference
    private FirebaseFirestore firestore;

    //map for adding the data
    Map<String, String > employee = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_employee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.empName);
        namet = view.findViewById(R.id.empNamet);
        username = view.findViewById(R.id.empUsername);
        usernamet = view.findViewById(R.id.empUsernamet);
        password = view.findViewById(R.id.empPassword);
        passwordt = view.findViewById(R.id.empPasswordt);
        cpassword = view.findViewById(R.id.confirmPassword);
        cpasswordt = view.findViewById(R.id.confirmPasswordt);
        add = view.findViewById(R.id.add);

        firestore = FirebaseFirestore.getInstance();

        username.setEnabled(false);
        password.setEnabled(false);
        cpassword.setEnabled(false);
        add.setEnabled(false);
        add.setBackgroundResource(R.drawable.btn_round_corners_d);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")) {
                    namet.setError("Enter Employee Name");
                    username.setEnabled(false);
                }
                else {
                    namet.setError(null);
                    username.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isWhiteSpace = charSequence.toString().matches("");
                if (charSequence.toString().equals("")) {
                    usernamet.setError("Enter Username");
                    password.setEnabled(false);
                }
                else if (charSequence.toString().contains(" ")) {
                    usernamet.setError("Username should not contain spaces");
                    password.setEnabled(false);
                }
                else if (charSequence.toString().length()>15){
                    usernamet.setError("Username should be less than 15 characters in length");
                    password.setEnabled(false);
                }
                else {
                    usernamet.setError(null);
                    password.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //for password validation
        String upperCaseChars = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";
        String numbers = "(.*[0-9].*)";
        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pass = charSequence.toString();
                if (pass.equals("")) {
                    passwordt.setError("Enter Password");
                    cpassword.setEnabled(false);
                }
                else if (pass.length()>15 || pass.length()<8) {
                    passwordt.setError("Password should be less than 15 and more than 8 characters in length");
                    cpassword.setEnabled(false);
                }
                else if (!pass.matches(numbers ))
                {
                    passwordt.setError("Password should contain atleast one number.");
                    cpassword.setEnabled(false);
                }
                else if (!pass.matches(specialChars ))
                {
                    passwordt.setError("Password should contain atleast one special character");
                    cpassword.setEnabled(false);
                }
                else {
                    passwordt.setError(null);
                    cpassword.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(password.getText().toString())) {
                    cpasswordt.setError(" ");
                    add.setEnabled(false);
                    add.setBackgroundResource(R.drawable.btn_round_corners_d);
                }
                else {
                    cpasswordt.setError(null);
                    add.setEnabled(true);
                    add.setBackgroundResource(R.drawable.btn_round_corners);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                empName = name.getText().toString();
                empUsername = username.getText().toString();
                empPassword = password.getText().toString();
                empImg = "https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg";

                firestore.collection("employees").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        int flag = 0;
                        for(DocumentSnapshot doc: value) {

                            if(error!=null) {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }

                            if(empUsername.equals(doc.getString("username"))) {
                                usernamet.setError("Username already exists");
                                flag = 1;
                                break;
                            }
                        }
                        if(flag == 1) {
                            return;
                        }
                        else {

                            employee.put("name", empName);
                            employee.put("username", empUsername);
                            employee.put("password", empPassword);
                            employee.put("img", empImg);
                            employee.put("completedTasks", "0");
                            employee.put("taskCount", "0");

                            firestore.collection("employees").document(empUsername).set(employee);

                            Toast.makeText(context, "Employee added successfully", Toast.LENGTH_SHORT).show();

                            dismiss();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
