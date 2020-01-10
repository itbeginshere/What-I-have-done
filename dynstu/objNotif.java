package com.example.dynstu;

public class objNotif{

    private String code;
    private String message;

    public objNotif(String code, String message){
        this.code = code;
        this.message = message;
    }

    public void setCode(String value){
        this.code = value;
    }

    public void setMessage(String value){
        this.message = value;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
