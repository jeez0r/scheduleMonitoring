package com.example.schedulemonitoring.Model;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.Date;

public class ModelOTSched{
    private String xremarks,xdateIn,xtimeout;
    private long xtotalOTMilisec;
    private Integer xnightdiff;
    private Integer xid;

    public ModelOTSched(){

    }

    public ModelOTSched(String xremarks, String xdateIn, String xtimeout, long xtotalOTMilisec, Integer xnightdiff, Integer xid) {
        this.xremarks = xremarks;
        this.xdateIn = xdateIn;
        this.xtimeout = xtimeout;
        this.xtotalOTMilisec = xtotalOTMilisec;
        this.xnightdiff = xnightdiff;
        this.xid = xid;
    }

    public String getXremarks() {
        return xremarks;
    }

    public void setXremarks(String xremarks) {
        this.xremarks = xremarks;
    }

    public String getXdateIn() {
        return xdateIn;
    }

    public void setXdateIn(String xdateIn) {
        this.xdateIn = xdateIn;
    }

    public String getXtimeout() {
        return xtimeout;
    }

    public void setXtimeout(String xtimeout) {
        this.xtimeout = xtimeout;
    }

    public long getXtotalOTMilisec() {
        return xtotalOTMilisec;
    }

    public void setXtotalOTMilisec(long xtotalOTMilisec) {
        this.xtotalOTMilisec = xtotalOTMilisec;
    }

    public Integer getXnightdiff() {
        return xnightdiff;
    }

    public void setXnightdiff(Integer xnightdiff) {
        this.xnightdiff = xnightdiff;
    }

    public Integer getXid() {
        return xid;
    }

    public void setXid(Integer xid) {
        this.xid = xid;
    }
}
