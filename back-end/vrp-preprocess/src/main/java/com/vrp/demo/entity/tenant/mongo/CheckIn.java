package com.vrp.demo.entity.tenant.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn {

    private Float lat;
    private Float lng;
    private String image;
    private String description;
    private Date createdAt;
    private Date updatedAt;
}
