package com.example.dynstu;

public class objSchedule {

    private String date;
    private String type;
    private String room;
    private String start;
    private String end;
    private String cap;
    private String code;
    private String price;
    private String full;

    public objSchedule(String date, String type, String room, String start, String end, String cap, String code, String price, String full) {
        this.date = date;
        this.type = type;
        this.room = room;
        this.start = start;
        this.end = end;
        this.cap = cap;
        this.code = code;
        this.price = price;
        this.full = full;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getRoom() {
        return room;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getCap() {
        return cap;
    }

    public String getCode() {
        return code;
    }

    public String getPrice() {
        return price;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
