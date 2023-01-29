package com.vrp.demo.models.driver;

import com.vrp.demo.entity.common.User;
import com.vrp.demo.entity.tenant.Vehicle;
import com.vrp.demo.models.search.BaseSearch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponsive {
    private User user;
    private Vehicle vehicle;
}
