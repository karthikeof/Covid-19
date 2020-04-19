package com.example.covid;

public class Patient {
    private Long PatientNum;
    private String Age;
    private String Notes;
    private String District;
    private String gender;
    private String status;
    private String date;





    public Patient(Long PatientNum,String Age, String Notes,String District,String gender,String status,String date)
    {
        this.PatientNum = PatientNum;
        this.Age = Age;
        this.Notes = Notes;
        this.District=District;
        this.gender=gender;
        this.status=status;
        this.date=date;

    }
    /*public void setSortBy(String SortBy)
    {
        this.SortBy=SortBy;
    }*/




    public Long getPatientNum() {
        return PatientNum;
    }
    public String getAge() {
        return Age;
    }
    public String getNotes() {
        return Notes;
    }
    public String getDistrict() {
        return District;
    }
    public String getgender(){return gender;}
    public String getstatus(){return status;}
    public String getdate(){return date;}





}
