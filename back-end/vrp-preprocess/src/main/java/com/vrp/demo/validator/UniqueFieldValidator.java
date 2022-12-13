package com.vrp.demo.validator;

import com.vrp.demo.service.BaseService;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

public class UniqueFieldValidator implements ConstraintValidator<UniqueField, Object> {

    private static final Logger log = LoggerFactory.getLogger(UniqueFieldValidator.class);

    private String fieldName;
    private Class entityClass;
    private String message;

    @Autowired
    @Qualifier(value = "userService")
    private BaseService baseService;

    @Override
    public void initialize(final UniqueField constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.entityClass = constraintAnnotation.entityClass();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object object, final ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            String value = BeanUtils.getProperty(object, fieldName);
            String idStr = BeanUtils.getProperty(object, "id");
            if (StringUtils.isEmpty(value)) {
                valid = false;
            } else {
                Long id = null;
                if (idStr != null) {
                    id = Long.parseLong(idStr);
                }
                valid = baseService.isUnique(this.entityClass, id, this.fieldName, value);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error(e.getMessage(), e);
        }
        if (!valid) {
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(fieldName).addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;
    }
}
