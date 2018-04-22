package com.odatalib.main;

import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.filters.IResponseFilter;
import com.sap.smp.client.httpc.filters.IResponseFilterChain;

import java.io.IOException;
import java.util.List;

/**
 * Created by 972391 on 1/10/2017.
 */
public class XCSRFTokenResponseFilter implements IResponseFilter {

    private static final String TAG = XCSRFTokenResponseFilter.class.getSimpleName();
    private XCSRFTokenRequestFilter requestFilter;

    public static XCSRFTokenResponseFilter getInstance(XCSRFTokenRequestFilter requestFilter) {
        return new XCSRFTokenResponseFilter(requestFilter);
    }

    private XCSRFTokenResponseFilter(XCSRFTokenRequestFilter requestFilter) {
        this.requestFilter = requestFilter;
    }

    @Override
    public Object filter(IReceiveEvent iReceiveEvent, IResponseFilterChain iResponseFilterChain) throws IOException {

        List<String> xcsrfTokens = iReceiveEvent.getResponseHeaders().get(Constants.XCSRFTOKEN);

        if (xcsrfTokens != null && xcsrfTokens.size() > 0) {
            String token = xcsrfTokens.get(0);
            if (token.equalsIgnoreCase(Constants.REQUIRED)) {
                token = "yeMKc0D9DZAuvFzeMr2BKA==";
            }
            requestFilter.setLastXCSRFToken(token);
        }
        return iResponseFilterChain.filter();
    }

    @Override
    public Object getDescriptor() {
        return TAG;
    }
}
