package com.fileencrypter.model;

public class Context {
    //singleton for passing data between tabs
    private final static Context instance = new Context();

    private String password;

    public static Context getInstance() {return  instance;};


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
