package com.example.john.qciandroidapp;

import java.util.Date;

/**
 * Created by John on 18/12/2017.
 */

public class Apt {
    private String email;
    private Date slot;
    private String advisor;

    public Apt(String email, Date slot, String advisor) {
        this.advisor = advisor;
        this.email = email;
        this.slot = slot;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getSlot() {
        return slot;
    }

    public void setSlot(Date slot) {
        this.slot = slot;
    }

    public String getAdvisor() {
        return advisor;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }
}
