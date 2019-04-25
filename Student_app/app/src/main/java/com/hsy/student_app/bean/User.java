package com.hsy.student_app.bean;

public class User {

    int id;
    int token;
    int state;
    String name;
    String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    String joson = "{\n" +
            "\t\"id\": \"001\",\n" +
            "\t\"token\": \"001001001\",\n" +
            "\t\"state\": 0,\n" +
            "\t\"name\": \"123\",\n" +
            "\t\"password\": \"123\"\n" +
            "}";
}
