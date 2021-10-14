package com.technumopus.moneylisa.model;

public class UserBean {
    int user_id;
    String user_name;
    String user_email;
    String user_password;
    String default_currency;
    float user_threshold;


    public UserBean() {
    }

    public UserBean(String user_email, String user_password) {
        this.user_email = user_email;
        this.user_password = user_password;
    }

    public int get_user_id() {
        return user_id;
    }

    public String get_user_name() {
        return user_name;
    }

    public String get_user_email() { return user_email; }

    public String get_user_password() { return user_password; }

    public String get_default_currency() { return default_currency; }

    public float get_user_threshold() { return user_threshold; }

    public void set_user_name(String user_name) {
        this.user_name = user_name;
    }

    public void set_user_email(String user_email) {
        this.user_email = user_email;
    }

    public void set_user_password(String user_password) {
        this.user_password = user_password;
    }

    public void set_default_currency(String default_currency) { this.default_currency = default_currency; }

    public void set_user_threshold(float user_threshold) { this.user_threshold = user_threshold; }

    @Override
    public String toString() {
        return "UserBean{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_password='" + user_password + '\'' +
                ", default_currency='" + default_currency + '\'' +
                ", user_threshold='" + user_threshold + '\'' +
                '}';
    }
}
