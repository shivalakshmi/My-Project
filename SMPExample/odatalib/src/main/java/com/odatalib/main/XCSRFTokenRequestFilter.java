package com.odatalib.main;

import com.odatalib.main.models.RegisteredUser;
import com.sap.smp.client.httpc.HttpMethod;
import com.sap.smp.client.httpc.events.ISendEvent;
import com.sap.smp.client.httpc.filters.IRequestFilter;
import com.sap.smp.client.httpc.filters.IRequestFilterChain;

/**
 * Created by 972391 on 1/9/2017.
 */
public class XCSRFTokenRequestFilter implements IRequestFilter {

    private static final String HTTP_HEADER_SUP_APPCID = "X-SUP-APPCID";
    private static final String HTTP_HEADER_SMP_APPCID = "X-SMP-APPCID";
    private String lastXCSRFToken = null;
    private static final String TAG = XCSRFTokenRequestFilter.class.getSimpleName();

    private XCSRFTokenRequestFilter() {

    }

    public static XCSRFTokenRequestFilter getInstance() {

        return new XCSRFTokenRequestFilter();
    }


    @Override
    public Object filter(ISendEvent iSendEvent, IRequestFilterChain iRequestFilterChain) {

        HttpMethod httpMethod = iSendEvent.getMethod();
        if (httpMethod == HttpMethod.GET) {
            iSendEvent.getRequestHeaders().put(Constants.XCSRFTOKEN, Constants.FETCH);
        } else if (lastXCSRFToken != null) {
            iSendEvent.getRequestHeaders().put(Constants.X_REQUEST_WITH, lastXCSRFToken);
        } else {
            iSendEvent.getRequestHeaders().put(Constants.X_REQUEST_WITH, Constants.X_REQUEST_TYPE);
        }

        String appConnID = RegisteredUser.getInstance().getAppConnectionId();

        if (appConnID != null) {
            iSendEvent.getRequestHeaders().put(HTTP_HEADER_SUP_APPCID, appConnID);
            iSendEvent.getRequestHeaders().put(HTTP_HEADER_SMP_APPCID, appConnID);
        }
        iSendEvent.getRequestHeaders().put(Constants.CONNECTION, Constants.KEEP_ALIVE);

        return iRequestFilterChain.filter();
    }

    @Override
    public Object getDescriptor() {
        return TAG;
    }
    public void setLastXCSRFToken(String lastXCSRFToken) {
        this.lastXCSRFToken = lastXCSRFToken;
    }
}
