package com.easychat.sse.advice;

import com.easychat.sse.exception.CustomRuntimeException;
import com.easychat.sse.response.R;
import com.easychat.sse.utils.ValidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {


    @ExceptionHandler(CustomRuntimeException.class)
    public Object handleCustomerException(CustomRuntimeException exception, HttpServletRequest request) {
        if (ValidUtil.isAjax(request)) {
            log.error(exception.getMessage(), exception);
            return R.fail(exception.getMessage());
        }
        return new ModelAndView("500");
    }


    @ExceptionHandler(Exception.class)
    public Object handleException(Exception exception, HttpServletRequest request) {
        if (ValidUtil.isAjax(request)) {
            log.error(exception.getMessage(), exception);
            return R.fail(exception.getMessage());
        }
        return new ModelAndView("500");
    }


}
