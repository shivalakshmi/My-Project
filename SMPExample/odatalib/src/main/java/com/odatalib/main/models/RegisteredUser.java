package com.odatalib.main.models;

/**
 * Created by 972391 on 1/6/2017.
 */
public class RegisteredUser {

    private String userName;
    private String password;
    private String appConnectionId;
    private static RegisteredUser registeredUser;

    public static RegisteredUser getInstance() {
        if (registeredUser == null) {
            registeredUser = new RegisteredUser();
        }
        return registeredUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppConnectionId() {
        return appConnectionId;
    }

    public void setAppConnectionId(String appConnectionId) {
        this.appConnectionId = appConnectionId;
    }
}
