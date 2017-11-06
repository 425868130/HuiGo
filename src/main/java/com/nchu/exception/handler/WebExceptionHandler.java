package com.nchu.exception.handler;

import com.nchu.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.ConnectException;

/**
 * 2017-9-25 10:55:52
 * web层统一异常处理器
 */
@ControllerAdvice
public class WebExceptionHandler {
    @ExceptionHandler(ConnectException.class)
    public void operateExpNetException(ConnectException ex, HttpServletResponse response) throws IOException {
        System.out.println("捕获异常");
        //将Ajax异常信息回写到前台，用于页面的提示
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("sorry,网络连接出错！！！");
    }

    @ExceptionHandler(IOException.class)
    public @ResponseBody
    ResponseEntity<ServiceException> ioExpHandler(IOException ex) throws IOException {
        return new ResponseEntity<>(new ServiceException("文件读写异常 "), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    public @ResponseBody
    ResponseEntity<ServiceException> ServiceExceptionHandler(ServiceException userExc) throws IOException {
        // userExc.printStackTrace();
        return new ResponseEntity<>(userExc, HttpStatus.NOT_FOUND);
    }
}
