package com.odatalib.main.interfaces;

import com.odatalib.main.models.RegisteredUser;

/**
 * Created by 972391 on 1/6/2017.
 */
public interface RegistrationInterface {
    void onRegistrationCompleted(RegisteredUser registeredUser);
    void onRegistrationFailed(String errorMessage,int errorCode);
}
