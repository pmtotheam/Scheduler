package com.example.hr.clientprototype;

/**
 * Created by H$R on 10/27/2018.
 */

public class Appointment {
    private String docname,specality,date;

    public Appointment(String docname, String specality, String date) {
        this.docname = docname;
        this.specality = specality;
        this.date = date;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getSpecality() {
        return specality;
    }

    public void setSpecality(String specality) {
        this.specality = specality;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
