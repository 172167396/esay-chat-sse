package com.easychat.sse.frame;

import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.response.R;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({CustomRuntimeException.class})
    public Object customerExceptionHandler(Exception e, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            return R.fail(e.getMessage());
        } else {
            HashMap<String, Object> map = Maps.newHashMap();
            map.put("msg", e.getMessage());
            return new ModelAndView("/500", map);
        }
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
    }
}
