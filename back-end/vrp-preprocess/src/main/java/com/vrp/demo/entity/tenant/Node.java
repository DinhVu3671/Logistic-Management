package com.vrp.demo.entity.tenant;

import com.vrp.demo.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Node extends BaseEntity {

    private String code;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    @Column(name = "start_time")
    private Long startTime;
    @Column(name = "end_time")
    private Long endTime;
    @Transient
    private List<Correlation> correlations;

}
