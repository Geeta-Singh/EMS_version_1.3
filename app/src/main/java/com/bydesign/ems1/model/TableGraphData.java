package com.bydesign.ems1.model;

/**
 * Created by user on 7/12/2016.
 */
public class TableGraphData  {
    String deviceNAme;
    String tableDetails;
    int checkflag=0;

    public int getCheckflag() {
        return checkflag;
    }

    public void setCheckflag(int checkflag) {
        this.checkflag = checkflag;
    }

    public String getDeviceNAme() {
        return deviceNAme;
    }

    public void setDeviceNAme(String deviceNAme) {
        this.deviceNAme = deviceNAme;
    }

    public String getTableDetails() {
        return tableDetails;
    }

    public void setTableDetails(String tableDetails) {
        this.tableDetails = tableDetails;
    }
}
