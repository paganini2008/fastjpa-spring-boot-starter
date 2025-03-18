package com.github.fastjpa;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;

/**
 * 
 * @Description: Function
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public class Function<T> implements Field<T> {

    private final String represent;
    private final Class<T> resultClass;
    private final Field<?>[] fields;

    Function(String represent, Class<T> resultClass, Field<?>[] fields) {
        this.represent = represent;
        this.resultClass = resultClass;
        this.fields = fields;
    }

    public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
        Expression<?>[] args = new Expression<?>[fields != null ? fields.length : 0];
        int i = 0;
        for (Field<?> field : fields) {
            args[i++] = field.toExpression(model, builder);
        }
        return builder.function(represent, resultClass, args);
    }

    public String toString() {
        String s = StringUtils.repeat("%s", fields.length);
        List<String> args = new ArrayList<String>();
        args.add(represent);
        for (Field<?> field : fields) {
            args.add(field.toString());
        }
        return String.format("%s(" + s + ")", args.toArray());
    }

    public static <T> Function<T> build(String represent, Class<T> resultClass, String... attributeNames) {
        Field<?>[] fields = new Field[attributeNames.length];
        int i = 0;
        for (String attributeName : attributeNames) {
            fields[i] = Property.forName(attributeName);
        }
        return new Function<T>(represent, resultClass, fields);
    }

    public static <T> Function<T> build(String represent, Class<T> resultClass, Field<?>... fields) {
        return new Function<T>(represent, resultClass, fields);
    }

}
