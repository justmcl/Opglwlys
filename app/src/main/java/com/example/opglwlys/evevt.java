package com.example.opglwlys;

public class evevt {
    private int id;
    private String name;
    private String startTime;
    private String endTime;
    private String type;
    private int state;



//    private int pages;
//    private float price;

    public evevt(int id, String name, String startTime, String endTime, String type,int state) {
        super();
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.state = state;

    }
    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getstartTime() {
        return startTime;
    }

    public void setstartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getendTime() {
        return endTime;
    }

    public void setendTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getstate() {
        return state;
    }

    public void setstate(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return  id+","+ name + "," + startTime + ", " + endTime+ ", " + type;
    }

}
