package com.vrp.demo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long createdAt;
    private Long updatedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean deleted;
}
