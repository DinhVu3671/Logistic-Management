package com.vrp.demo.models.enu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Code {

    SUCCESS("success"),
    FAIL("fail"),
    UNKNOWN_ERROR("unknown.error"),
    METHOD_NOT_SUPPORT("method.not.support"),
    VALIDATION_FAILED("validation.failed"),
    USER_ID_EXISTED("user.id.existed"),
    EMAIL_EXISTED("user.email.existed"),
    USER_ID_NOT_EXISTED("user.id.not.existed"),
    CUSTOMER_ID_NOT_EXISTED("customer.id.not.existed"),
    DEPOT_ID_NOT_EXISTED("depot.id.not.existed"),
    VEHICLE_ID_NOT_EXISTED("vehicle.id.not.existed"),
    PRODUCT_ID_NOT_EXISTED("product.id.not.existed"),
    GOODS_GROUP_ID_NOT_EXISTED("goods.group.id.not.existed"),
    ORDER_ITEM_ID_NOT_EXISTED("order.item.id.not.existed"),
    ORDER_ID_NOT_EXISTED("order.id.not.existed"),
    CORRELATION_ID_NOT_EXISTED("correlation.id.not.existed"),
    VEHICLE_PRODUCT_ID_NOT_EXISTED("vehicle-product.id.not.existed"),
    LOGIN_FAIL("login.fail"),
    LOGIN_SUCCESS("login.success"),
    ;

    private final String description;

}
