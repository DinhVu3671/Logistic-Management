package com.vrp.demo.models.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPassword {

    private String userEmail;
    private String oldPassword;
    private String newPassword;

}
