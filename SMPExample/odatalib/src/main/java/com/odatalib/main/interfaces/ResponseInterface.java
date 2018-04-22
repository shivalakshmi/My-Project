package com.odatalib.main.interfaces;

/**
 * Created by 972391 on 1/5/2017.
 */
public interface ResponseInterface {

    void didReceiveData(Object data);
    void didReceiveError(String message, int statusCode);
}
