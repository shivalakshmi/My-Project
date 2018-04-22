package com.odataexample.utils;

/**
 * Created by 972391 on 1/5/2017.
 */
public class ServiceConstants {

    public static final String APPLICATION_NAME = "com.al.inc.dealmaker";

    // port and initial url details for registration
    public static final boolean HTTPS_ENABLE = true;
    public static final String HOST_NAME = "smpqproxy1.ashokleyland.com";
    public static final int PORT = 443;

    //offline url's configured in SMP admin cockpit.

    public static final String SERVICE_ROOT_LOV = "com.al.inc.dealmaker.lov";
    public static final String SERVICE_ROOT_USER_PROFILE = "com.al.inc.dealmaker.login";

    // sets in service root lov

    public static String CITYSET = "CitySet";
    public static String MODEL_SET = "ModelSet";
    public static String LOGIN_SET = "ZloginSet";


    //online store URL's

    public static final String CONNECTION_ID_USER_PROFILE = getBaseURL() + SERVICE_ROOT_USER_PROFILE;
    public static final String CONNECTION_ID_LOV_CALLS = getBaseURL() + SERVICE_ROOT_LOV;

    // names spaces are nothing but service names provided by backend team / smp administrator

    public static final String NAME_SPACE_USER_PROFILE = "ZCRM_DM_LOGIN_SRV";
    public static final String NAME_SPACE_LOV = "ZCRM_DM_LOV_SRV";

    private static String getBaseURL() {

        StringBuilder baseUrl = new StringBuilder();
        if (HTTPS_ENABLE) {
            baseUrl.append("https://");
        } else {
            baseUrl.append("http://");
        }
        if (!HOST_NAME.isEmpty()) {
            baseUrl.append(HOST_NAME);
        }
        baseUrl.append(":").append(PORT).append("/");

        return baseUrl.toString();
    }

}
