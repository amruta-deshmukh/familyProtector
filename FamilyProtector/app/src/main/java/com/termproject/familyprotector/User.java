package com.termproject.familyprotector;

/**
 * Created by Mehul on 10/8/2015.
 */
public class User {
    public String username, password, message;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(String message){
        this.message = message;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;

    }
    public String getMessage(){
        return message;

    }

    public void setMessage(String message){
        this.message = message;

    }


}
