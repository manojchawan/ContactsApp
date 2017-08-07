package com.android.manoj.contactsapp;

/**
 * Created by manojc on 8/7/2017.
 */

public class User {

    private String name;
    private String phone1;
    private String phone2;
    private String email;

    public User(String name, String phone1, String phone2, String email) {


        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getEmail() {
        return email;
    }

}
