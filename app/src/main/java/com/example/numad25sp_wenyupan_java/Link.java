package com.example.numad25sp_wenyupan_java;

import java.io.Serializable;

public class Link implements Serializable {
    private String name;
    private String phoneNumber;

    public Link() {}

    public Link(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
