package com.example.schedulemonitoring.Model;

public class ModelSettings {

    private Integer xID;
    private String xOfficeHour,xRemainders;
    private int xHourRate,xNightDiff;

    public ModelSettings() {
    }

    public ModelSettings(Integer xID, String xOfficeHour, String xRemainders, int xHourRate, int xNightDiff) {
        this.xID = xID;
        this.xOfficeHour = xOfficeHour;
        this.xRemainders = xRemainders;
        this.xHourRate = xHourRate;
        this.xNightDiff = xNightDiff;
    }

    public Integer getxID() {
        return xID;
    }

    public void setxID(Integer xID) {
        this.xID = xID;
    }

    public String getxOfficeHour() {
        return xOfficeHour;
    }

    public void setxOfficeHour(String xOfficeHour) {
        this.xOfficeHour = xOfficeHour;
    }

    public String getxRemainders() {
        return xRemainders;
    }

    public void setxRemainders(String xRemainders) {
        this.xRemainders = xRemainders;
    }

    public int getxHourRate() {
        return xHourRate;
    }

    public void setxHourRate(int xHourRate) {
        this.xHourRate = xHourRate;
    }

    public int getxNightDiff() {
        return xNightDiff;
    }

    public void setxNightDiff(int xNightDiff) {
        this.xNightDiff = xNightDiff;
    }
}
