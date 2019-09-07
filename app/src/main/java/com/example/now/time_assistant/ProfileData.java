package com.example.now.time_assistant;

public class ProfileData {

    String user_name;
    String user_nickname;
    String user_profile_img;
    String user_profile_backimg;
    String user_email;
    String user_phonenum;

    public ProfileData(String user_name, String user_nickname, String user_profile_img,
                       String user_profile_backimg, String user_email, String user_phonenum) {
        this.user_name = user_name;
        this.user_nickname = user_nickname;
        this.user_profile_img = user_profile_img;
        this.user_profile_backimg = user_profile_backimg;
        this.user_email = user_email;
        this.user_phonenum = user_phonenum;
    }

    public String getUser_profile_backimg() {
        return user_profile_backimg;
    }

    public void setUser_profile_backimg(String user_profile_backimg) {
        this.user_profile_backimg = user_profile_backimg;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phonenum() {
        return user_phonenum;
    }

    public void setUser_phonenum(String user_phonenum) {
        this.user_phonenum = user_phonenum;
    }

    public String getUser_profile_img() {
        return user_profile_img;
    }

    public void setUser_profile_img(String user_profile_img) {
        this.user_profile_img = user_profile_img;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }
}
