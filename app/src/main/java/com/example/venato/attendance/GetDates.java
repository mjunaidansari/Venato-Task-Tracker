package com.example.venato.attendance;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GetDates {

    private static final String TAG = "GetDates";

    List<String> dates = new ArrayList<>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public GetDates() {}
    public GetDates(String username) {fillDates(username);}

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public void fillDates(String username) {

        firestore.collection("employees").document(username).collection("attendance").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(DocumentSnapshot doc: task.getResult()) {
                        String date = doc.getString("date");
                        dates.add(date);
                    }
                    Log.d(CalendarBottomSheetFragment.TAG, "onEvent: " + dates.toString());
                }
                else {
                    Log.d(CalendarBottomSheetFragment.TAG, "onComplete: Error in getting documents " + task.getException());
                }
            }
        });

    }


}
