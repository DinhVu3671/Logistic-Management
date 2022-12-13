package com.vrp.demo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UniqueFieldValidator.class)
@Documented
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface UniqueField {
    String message() default "This field is already existed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String fieldName();

    Class entityClass();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        UniqueField[] value();
    }
}
