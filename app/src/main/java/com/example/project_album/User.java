package com.example.project_album;

public class User {
    private String username;
    private String pass;
    private  String email;
    private String phone;
    User(String username, String pass,String email,String phone)
    {
        this.username=username;
        this.email=email;
        this.pass=pass;
        this.phone=phone;
    }
    public String getUsername(){return username;}
    public String getPass(){return  pass;}
    public String getEmail(){return email;}
    public String getPhone(){return phone;}
}
