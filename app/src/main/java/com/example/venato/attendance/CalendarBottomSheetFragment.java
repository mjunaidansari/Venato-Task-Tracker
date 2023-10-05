package com.example.venato.attendance;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.CalendarView;
import com.applandeo.materialcalendarview.CalendarView;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.adapters.CalendarPageAdapter;
import com.example.venato.R;
import com.example.venato.database.DataBaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class CalendarBottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = "CalendarBottomSheetFrag";

    Context context;

    String username;
    public void setUsername(String username) {
        this.username = username;
    }

    //layout views
    ImageView back;
    Button present;
    CalendarView calendarView;

    //data fetching
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    List<String> dates = new ArrayList<>();
    Map<String, String> attendance;

    //to check if constructor call is from admin panel
    String a;

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public CalendarBottomSheetFragment() {}

    public CalendarBottomSheetFragment(String a) {
        this.a = a;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.calendar_bottom_sheet_fragment, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back = view.findViewById(R.id.back);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        present = view.findViewById(R.id.present);

        if(a!=null) {
            present.setVisibility(View.GONE);
        }

        calendarView.setHeaderColor(R.color.blue_200);
        Log.d(TAG, "onViewCreated: " + dates.toString());
        calendarView.setEvents(getHighlightedDays());

        Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);
        String today = d + "-" + (m+1) + "-" +  y;
//        Toast.makeText(context, today, Toast.LENGTH_SHORT).show();

        firestore.collection("employees").document(username).collection("attendance").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null) {
                    Log.d(TAG, "onEvent: Error");
                    error.printStackTrace();
                }
                for(DocumentSnapshot doc: value) {
                    Log.d(TAG, "onEvent: " + doc.getString("date"));
                    if(today.equals(doc.getString("date"))) {
                        present.setVisibility(View.GONE);
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                String time = hour + ":" + minute;

                attendance = new HashMap<>();
                attendance.put("date", today);
                attendance.put("time", time);
                firestore.collection("employees").document(username).collection("attendance").add(attendance);
                present.setEnabled(false);
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    private List<EventDay> getHighlightedDays() {

        List<EventDay> events = new ArrayList<>();

        for(int i = 0; i < dates.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            String[] items1 = dates.get(i).split("-");
            String dd = items1[0];
            String month = items1[1];
            String year = items1[2];

            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dd));
            calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            calendar.set(Calendar.YEAR, Integer.parseInt(year));
            events.add(new EventDay(calendar, R.drawable.tick));
        }
        return events;
    }
}
