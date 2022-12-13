package com.vrp.demo.entity.common;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.models.UserModel;
import com.vrp.demo.models.UserSessionModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_sessions")
public class UserSession extends BaseEntity {

    private String sessionId;
    private Date expireDate;
    private boolean isExpired;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static UserSessionModel convertToModel(UserSession userSession){
        UserModel userModel = User.convertToModel(userSession.getUser(),true);
        ModelMapper modelMapper = new ModelMapper();
        UserSessionModel userSessionModel = modelMapper.map(userSession, UserSessionModel.class);
        userSessionModel.setUser(userModel);
        return userSessionModel;
    }

}
