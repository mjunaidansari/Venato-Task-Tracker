package com.example.venato.leave;

import java.io.Serializable;

public class AdminLeaveApplication implements Serializable {

    private String leaveId;
    private String leaveType;
    private String leaveDesc;
    private String startDate;
    private String endDate;
    private int status;
    private String remark;

    private String stringStatus;

    private String empUsername;
    private String empName;
    private String img;

    public AdminLeaveApplication() {}

    public AdminLeaveApplication(String leaveId, String leaveType, String leaveDesc, String startDate, String endDate, int status, String remark, String empUsername, String empName, String img) {
        this.leaveId = leaveId;
        this.leaveType = leaveType;
        this.leaveDesc = leaveDesc;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.remark = remark;
        this.empUsername = empUsername;
        this.empName = empName;
        this.img = img;
    }

    @Override
    public String toString() {
        return "AdminLeaveApplication{" +
                "leaveId='" + leaveId + '\'' +
                ", leaveType='" + leaveType + '\'' +
                ", leaveDesc='" + leaveDesc + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", empUsername='" + empUsername + '\'' +
                ", empName='" + empName + '\'' +
                ", img='" + img + '\'' +
                '}';
    }


    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveDesc() {
        return leaveDesc;
    }

    public void setLeaveDesc(String leaveDesc) {
        this.leaveDesc = leaveDesc;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStringStatus() {
        if(this.status == -1) {
            return "Pending";
        }
        else if (this.status == 0){
            return "Declined";
        }
        else {
            return "Approved";
        }
    }

    public String getEmpUsername() {
        return empUsername;
    }

    public void setEmpUsername(String empUsername) {
        this.empUsername = empUsername;
    }
    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
