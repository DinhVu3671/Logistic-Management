package com.vrp.demo.utils;

import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ResponsePreProcessor {

    @Autowired
    private MessageSource messageSource;

    public ResponseData buildResponseData(int status, Code code, Object data) {
        ResponseData responseData = new ResponseData();
        Locale currentLocale = CommonUtils.getCurrentLocale();
        responseData.setStatus(status);
        responseData.setCode(code);
        responseData.setMessage(messageSource.getMessage(code.getDescription(), null, currentLocale));
        responseData.setData(data);
        responseData.setTimestamp(CommonUtils.getCurrentTime());
        return responseData;
    }

    ;

    public ResponseEntity<ResponseData> buildResponseEntity(HttpStatus status, Code code, Object data) {
        ResponseData responseData = buildResponseData(status.value(), code, data);
        ResponseEntity<ResponseData> responseEntity = new ResponseEntity<ResponseData>(responseData, status);
        return responseEntity;
    }

    public ResponseEntity<ResponseData> buildResponseEntity(HttpStatus status, ResponseData responseData) {
        ResponseEntity<ResponseData> responseEntity = new ResponseEntity<ResponseData>(responseData, status);
        return responseEntity;
    }

}
