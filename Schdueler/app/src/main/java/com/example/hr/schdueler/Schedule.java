package com.example.hr.schdueler;

/**
 * Created by H$R on 10/21/2018.
 */

public class Schedule {
    private String day;
    private String from;
    private String ampmFrom;
    private String To;
    private String ampmTo;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAmpmFrom() {
        return ampmFrom;
    }

    public void setAmpmFrom(String ampmFrom) {
        this.ampmFrom = ampmFrom;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getAmpmTo() {
        return ampmTo;
    }

    public void setAmpmTo(String ampmTo) {
        this.ampmTo = ampmTo;
    }
}
