package com.example.dynstu;

public class objPasswords {
    private String email;
    private String firstName;
    private String lastName;

    public objPasswords(String email, String firstName, String lastName){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setEmail(String value){
        this.email = value;
    }

    public void setFirstName(String value){
        this.firstName = value;
    }

    public void setLastName(String value){
        this.firstName = value;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
