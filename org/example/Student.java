package org.example;

public class Student {
    private String StudentID;
    private String StudentName;
    private double fineAmount;
    Student(String StudentID,String StudentName,Double fineAmount){
        this.StudentID=StudentID;
        this.StudentName=StudentName;
        this.fineAmount=fineAmount;
    }
    public void setName(String Name) {
        this.StudentName = StudentName;
    }
    public String getStudentName() {
        return StudentName;
    }
    public void setStudentId(String StudentID) {
        this.StudentID = StudentID;
    }
    public String getStudentID() {
        return StudentID;
    }
    public void setFineAmount(double fineAmount) {
        this.fineAmount=fineAmount;
    }
    public double getFineAmount() {
        return fineAmount;
    }
}
