package com.example.venato.employeetasklist;

import java.util.prefs.PreferenceChangeListener;

public class Tasks {

    private String taskId;
    private String taskName;
    private String taskDetail;
    private String date;
    private String time;
    private String dueDate;
    private String dueTime;
    private int priority;
    private int status;

    private String ref;
    private String res;

    String deadLine;
    String assignedOn;

    public Tasks() {}

    public Tasks(String taskId, String taskName, String taskDetail, String date, String time, String dueDate, String dueTime, int priority, int status, String ref, String res) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDetail = taskDetail;
        this.date = date;
        this.time = time;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.priority = priority;
        this.status = status;
        this.ref = ref;
        this.res = res;
    }

    @Override
    public String toString() {
        return "Tasks{" +
                "taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskDetail='" + taskDetail + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", dueTime='" + dueTime + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", ref='" + ref + '\'' +
                ", res='" + res + '\'' +
                ", deadLine='" + deadLine + '\'' +
                ", assignedOn='" + assignedOn + '\'' +
                '}';
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String returnPriority() {
        if(this.priority == 0) {
            return "Low";
        }
        else if(this.priority == 1) {
            return "Medium";
        }
        else {
            return "High";
        }
    }

    public String returnStatus() {
        if(this.status == 0) {
            return "Due";
        } else {
            return "Completed";
        }
    }

    public String returnDeadline() {

        String[] timeSplit = this.dueTime.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        String min = timeSplit[1];

        if(hour>12) {
            hour = hour - 12;
            deadLine = this.dueDate + " " + hour + ":" + min + " AM";
            return deadLine;
        }
        else if (hour==12) {
            deadLine = this.dueDate + " " + this.dueTime + " AM";
            return deadLine;
        }
        else if (hour==0) {
            hour = 12;
            deadLine = this.dueDate + " " + hour + ":" + min + " PM";
            return deadLine;
        }
        else {
            deadLine = this.dueDate + " | " + this.dueTime + " AM";
            return deadLine;
        }

    }

    public String returnAssignedOn() {

        String[] timeSplit = this.time.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        String min = timeSplit[1];

        if(hour>12) {
            hour = hour - 12;
            assignedOn = this.date + " " + hour + ":" + min + "pm";
            return assignedOn;
        }
        else if (hour==12) {
            assignedOn = this.date + " " + this.time + "am";
            return assignedOn;
        }
        else if (hour==0) {
            hour = 12;
            assignedOn = this.date + " " + hour + ":" + min + "pm";
            return assignedOn;
        }
        else {
            assignedOn = this.date + " " + this.time + "am";
            return assignedOn;
        }

    }
}
