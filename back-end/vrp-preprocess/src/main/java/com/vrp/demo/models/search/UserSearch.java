package com.vrp.demo.models.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSearch extends BaseSearch {

    private String userName;
    private String email;
    private String fullName;
    private Long roleId;

}
