package com.example.venato.ui.home;

import java.util.Comparator;

public class Employees {

    private String username;
    private String name;
    private String img;
    private String password;
    private String taskCount;
    private String completedTasks;

    public Employees() {}

    public Employees(String username, String name, String img, String taskCount, String completedTasks) {
        this.username = username;
        this.name = name;
        this.img = img;
        this.taskCount = taskCount;
        this.completedTasks = completedTasks;
    }

    @Override
    public String toString() {
        return "Employees{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", password='" + password + '\'' +
                ", taskCount='" + taskCount + '\'' +
                ", completedTask='" + completedTasks + '\'' +
                '}';
    }

    public static Comparator<Employees> EmployeeNameAZComparator = new Comparator<Employees>() {
        @Override
        public int compare(Employees e1, Employees e2) {
            return e1.getName().compareTo(e2.getName());
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount;
    }

    public String getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(String completedTask) {
        this.completedTasks = completedTask;
    }
}
