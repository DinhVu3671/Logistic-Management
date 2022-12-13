package com.vrp.demo.utils;

import com.vrp.demo.models.UserModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GooglePojo {

    private String id;
    private String email;
    private boolean verified_email;
    private String name;
    private String given_name;
    private String family_name;
    private String link;
    private String picture;

    public UserModel convertToUserModel(){
        UserModel userModel = new UserModel();
        userModel.setUserName(email);
        userModel.setEmail(email);
        userModel.setFullName(name);
        userModel.setAvatar(picture);
        return userModel;
    }

}
