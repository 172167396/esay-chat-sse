package com.easychat.sse.utils;

import javax.servlet.http.HttpServletRequest;

public class ValidUtil {

    public static boolean isAjax(HttpServletRequest request){
        String contentTypeHeader = request.getHeader("Content-Type");
        String acceptHeader = request.getHeader("Accept");
        String xRequestedWith = request.getHeader("X-Requested-With");
        return ((contentTypeHeader != null && contentTypeHeader.contains("application/json"))
                || (acceptHeader != null && acceptHeader.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith));
    }
}
