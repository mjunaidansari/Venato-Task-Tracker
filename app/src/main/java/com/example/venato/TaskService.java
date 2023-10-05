package com.example.venato;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.venato.database.DataBaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Timer;
import java.util.TimerTask;

public class TaskService extends Service {

    private static final String TAG = "TaskService";

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Query query;
    ListenerRegistration registration;
    CollectionReference reference;

    DataBaseHelper db = new DataBaseHelper(this);

    String username;

    Timer timer = null;
    private Handler handler = new Handler();
    long timerPeriod = 5000;

    public void setUsername(Context context) {
        this.username = db.getUsername(context);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setUsername(this);
        Log.d(TAG, "onCreate: username:" + username);

        reference = firestore.collection("employee").document(username).collection("tasks");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                Log.d(TAG, "onEvent: called");

                if (error != null) {
                    Log.e(TAG, "onEvent: Error ", error );
                }
                for(DocumentChange documentChange: value.getDocumentChanges()) {
                    switch (documentChange.getType()) {
                        case ADDED:
                            Log.d(TAG, "onEvent: new task added");
                        case MODIFIED:
                            Log.d(TAG, "onEvent: task modified");
                        case REMOVED:
                            Log.d(TAG, "onEvent: task removed");
                    }
                }
            }
        });

//        timer = new Timer();
//        timer.schedule(new TimerTaskToGetTaskUpdate(), 1000, 1000);

//        readChanges();

    }

//    public TimerTask timerReadChanges() {
//        readChanges();
//        return null;
//    }

    private class TimerTaskToGetTaskUpdate extends TimerTask {

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    readChanges();
                }
            });
        }
    }

    public void readChanges() {

        reference = firestore.collection("employee").document("Junaid").collection("tasks");

//        EventListener<QuerySnapshot> eventListener = new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.e(TAG, "onEvent: Error ", error );
//                }
//                for(DocumentChange documentChange: value.getDocumentChanges()) {
//                    switch (documentChange.getType()) {
//                        case ADDED:
//                            Log.d(TAG, "onEvent: new task added");
//                        case MODIFIED:
//                            Log.d(TAG, "onEvent: task modified");
//                        case REMOVED:
//                            Log.d(TAG, "onEvent: task removed");
//                    }
//                }
//            }
//        };

        Log.d(TAG, "readChanges: called");

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                Log.d(TAG, "onEvent: called");
                
                if (error != null) {
                    Log.e(TAG, "onEvent: Error ", error );
                }
                for(DocumentChange documentChange: value.getDocumentChanges()) {
                    switch (documentChange.getType()) {
                        case ADDED:
                            Log.d(TAG, "onEvent: new task added");
                        case MODIFIED:
                            Log.d(TAG, "onEvent: task modified");
                        case REMOVED:
                            Log.d(TAG, "onEvent: task removed");
                    }
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer!=null) {
            timer.cancel();
        }
        stopSelf();
        Log.d(TAG, "onDestroy: Service Stopped");
    }
}
