package com.example.medi_consult;

public class Appointment {
    private int appointmentID;
    private int patientId;
    private int doctorId;
    private String date;
    private String time;
    private  String disease;
    private  String[] allergies;

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String[] getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies.split(",");

        for(int i=0;i<this.allergies.length;i++){
            this.allergies[i]=this.allergies[i].trim();
        }
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
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

    public Appointment(int appointmentID, int patientId, int doctorId, String date, String time,String disease,String allergies) {
        this.appointmentID = appointmentID;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.disease=disease;
        this.setAllergies(allergies);
    }
}
