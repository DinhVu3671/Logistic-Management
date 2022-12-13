package com.vrp.demo.models.response;

import com.vrp.demo.models.enu.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {

    private int status;
    private Code code;
    private String message;
    private Object data;
    private Timestamp timestamp;

}
