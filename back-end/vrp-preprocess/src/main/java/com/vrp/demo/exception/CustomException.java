package com.vrp.demo.exception;

import com.vrp.demo.models.enu.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends Exception {

    private Code errorCode;

}
