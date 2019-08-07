package com.example.now.time_assistant;

public class Appointment {

    //새로운 방 팔 때 저장될 데이터를 위한 클래스, 약속 이름과 그림 경로가 저장될 예정
    public String appointment_name;
    public String appointment_img;

    public Appointment(String appointment_name, String appointment_img) {
        this.appointment_name = appointment_name;
        this.appointment_img = appointment_img;
    }
}
