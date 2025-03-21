package com.github.fastjpa;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Description: FieldList
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public class FieldList extends ArrayList<Field<?>> {

    private static final long serialVersionUID = 1992730401503253851L;

    public FieldList() {}

    @SafeVarargs
    public FieldList(Field<?>... fields) {
        super(List.of(fields));
    }

    @SafeVarargs
    public <E> FieldList(SerializedFunction<E, ?>... functions) {
        if (functions != null && functions.length > 0) {
            for (SerializedFunction<E, ?> function : functions) {
                add(Property.forName(function));
            }
        }
    }

    public FieldList(String... attributeNames) {
        if (attributeNames != null && attributeNames.length > 0) {
            for (String attributeName : attributeNames) {
                add(Property.forName(attributeName));
            }
        }
    }

    public FieldList(String alias, String[] attributeNames) {
        if (attributeNames != null && attributeNames.length > 0) {
            for (String attributeName : attributeNames) {
                add(Property.forName(alias, attributeName));
            }
        }
    }

    public FieldList addField(String attributeName) {
        add(Property.forName(attributeName));
        return this;
    }

    public FieldList addField(String alias, String attributeName) {
        add(Property.forName(alias, attributeName));
        return this;
    }

    public FieldList addField(String attributeName, Class<?> requiredType) {
        add(Property.forName(attributeName, requiredType));
        return this;
    }

    public FieldList addField(String alias, String attributeName, Class<?> requiredType) {
        add(Property.forName(alias, attributeName, requiredType));
        return this;
    }

    public FieldList addField(Field<?> field) {
        add(field);
        return this;
    }

    public <E> FieldList addField(SerializedFunction<E, ?> function) {
        add(Property.forName(function));
        return this;
    }

}
