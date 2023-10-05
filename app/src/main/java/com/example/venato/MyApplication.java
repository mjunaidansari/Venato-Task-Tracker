package com.example.venato;

import android.app.Application;

import com.example.venato.employeetasklist.Tasks;
import com.example.venato.ui.home.Employees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyApplication extends Application {
    private static List<Employees> employeesList = new ArrayList<Employees>();
    private static List<Tasks> tasksList = new ArrayList<Tasks>();

    public MyApplication() {
        fillTasksList();
    }

    private void fillEmployeesList() {
//        Employees e0 = new Employees("Employee 1", "https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg");
//        Employees e1 = new Employees("Employee 2", "https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg");
//        Employees e2 = new Employees("Employee 3", "https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg");
//        Employees e3 = new Employees("Employee 4", "https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg");
//        Employees e4 = new Employees("Employee 5", "https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg");
//        Employees e5 = new Employees("Employee 6", "https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg");
//        Employees e6 = new Employees("Employee 7", "https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg");
//        Employees e7 = new Employees("Employee 8", "https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg");
//        Employees e8 = new Employees("Employee 9", "https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg");
//        Employees e9 = new Employees("Employee 10", "https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg");
//        Employees e10 = new Employees("Employee 11", "https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg");

//        employeesList.addAll(Arrays.asList(new Employees[]{e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10}));
    }

    public void fillTasksList() {
        Tasks t1 = new Tasks("task1", "Sample Task" ,"Sample Task Detail", "10/10/22", "12:00", "11/11/22", "12:00", 1, 1, "", "");

        tasksList.addAll(Arrays.asList(new Tasks[]{t1}));
    }

    public static List<Employees> getEmployeesList() {
        return employeesList;
    }

    public static void setEmployeesList(List<Employees> employeesList) {
        MyApplication.employeesList = employeesList;
    }

    public static List<Tasks> getTasksList() {
        return tasksList;
    }

    public static void setTasksList(List<Tasks> tasksList) {
        MyApplication.tasksList = tasksList;
    }
}
