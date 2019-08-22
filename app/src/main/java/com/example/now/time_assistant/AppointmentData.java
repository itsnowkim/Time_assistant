package com.example.now.time_assistant;

public class AppointmentData {

    //새로운 방 팔 때 저장될 데이터를 위한 클래스, 약속 이름과 그림 경로가 저장될 예정
    String appointment_name;
    String appointment_img;
    String appointment_date_created;
    //승우한테 물어보고 다시 바꾸자
    String date_start_end;
    //

    String appointment_time;

    /*public AppointmentData(String appointment_name, String appointment_img, String appointment_date_created,
                       String date_start_end, String appointment_time) {
        this.appointment_name = appointment_name;
        this.appointment_img = appointment_img;
        this.appointment_date_created = appointment_date_created;
        this.date_start_end = date_start_end;
        this.appointment_time = appointment_time;
    }
    */

    public String getAppointment_name() {
        return appointment_name;
    }

    public void setAppointment_name(String appointment_name) {
        this.appointment_name = appointment_name;
    }

    public String getAppointment_img() {
        return appointment_img;
    }

    public void setAppointment_img(String appointment_img) {
        this.appointment_img = appointment_img;
    }

    public String getAppointment_date_created() {
        return appointment_date_created;
    }

    public void setAppointment_date_created(String appointment_date_created) {
        this.appointment_date_created = appointment_date_created;
    }

    public String getDate_start_end() {
        return date_start_end;
    }

    public void setDate_start_end(String date_start_end) {
        this.date_start_end = date_start_end;
    }

    //사실 얘는 어떻게 쓸지 잘 모르겠다
    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }
}

