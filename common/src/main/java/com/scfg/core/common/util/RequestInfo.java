package com.scfg.core.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestInfo {

    public String getUserAgent() {
        ServletRequestAttributes requestTemp = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestTemp != null) {
            return requestTemp.getRequest().getHeader("user-agent");
        } else {
            return "";
        }
    }

    public String getUserId() {
        ServletRequestAttributes requestTemp = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestTemp != null) {
            HttpServletRequest req = requestTemp.getRequest();
            return req.getHeader("userId");
        } else {
            return null;
        }
    }

}
