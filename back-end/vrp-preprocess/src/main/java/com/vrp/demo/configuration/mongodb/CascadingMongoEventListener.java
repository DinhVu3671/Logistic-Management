package com.vrp.demo.configuration.mongodb;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.MappingException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.util.ReflectionUtils;

@SuppressWarnings("rawtypes")
public class CascadingMongoEventListener extends AbstractMongoEventListener<Object> {
    @Inject
    private MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {

        ReflectionUtils.doWithFields(event.getSource().getClass(), new ReflectionUtils.FieldCallback() {

            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(field);

                if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeSave.class)) {
                    final Object fieldValue = field.get(event.getSource());
                    if (fieldValue != null && !(fieldValue instanceof List)) {
                        DbRefFieldCallback callback = new DbRefFieldCallback();
                        ReflectionUtils.doWithFields(fieldValue.getClass(), callback);
                        mongoOperations.save(fieldValue);
                    }
                    if (fieldValue instanceof List) {
                        List<Object> list = (List<Object>) fieldValue;
                        mongoOperations.insertAll(list);
                    }

                }


            }
        });
    }

    private static class DbRefFieldCallback implements ReflectionUtils.FieldCallback {
        private boolean idFound;

        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            ReflectionUtils.makeAccessible(field);

            if (field.isAnnotationPresent(Id.class)) {
                idFound = true;
            }
        }

        public boolean isIdFound() {
            return idFound;
        }
    }
}