package com.vrp.demo.entity;

import com.vrp.demo.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(name = "created_at")
    protected Long createdAt;
    @Column(name = "updated_at")
    protected Long updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = CommonUtils.getCurrentTime().getTime();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = CommonUtils.getCurrentTime().getTime();
    }


}
