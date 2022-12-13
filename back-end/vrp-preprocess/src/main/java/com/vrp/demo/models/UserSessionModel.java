package com.vrp.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionModel extends BaseModel{

    private String sessionId;
    private Date expireDate;
    private UserModel user;

}
