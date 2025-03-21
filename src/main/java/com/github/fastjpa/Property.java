package com.github.fastjpa;

import org.apache.commons.lang3.StringUtils;
import com.github.fastjpa.LambdaUtils.LambdaInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;

/**
 * 
 * @Description: Property
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public final class Property<T> implements Field<T> {

    private final String alias;
    private final String attributeName;
    private final Class<T> requiredType;

    Property(String alias, String attributeName, Class<T> requiredType) {
        this.alias = StringUtils.isNotBlank(alias) ? alias : Model.ROOT;
        this.attributeName = attributeName;
        this.requiredType = requiredType;
    }

    public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
        Expression<T> expression = model.getAttribute(alias, attributeName);
        if (requiredType != null) {
            return expression.as(requiredType);
        }
        return expression;
    }

    public String toString() {
        return String.format("%s.%s", alias, attributeName);
    }

    public static <T> Property<T> forName(String attributeName) {
        return forName(null, attributeName);
    }

    public static <T> Property<T> forName(String alias, String attributeName) {
        return forName(alias, attributeName, null);
    }

    public static <T> Property<T> forName(String attributeName, Class<T> requiredType) {
        return forName(null, attributeName, requiredType);
    }

    public static <T> Property<T> forName(String alias, String attributeName,
            Class<T> requiredType) {
        return new Property<T>(alias, attributeName, requiredType);
    }

    public static <E, T> Property<T> forName(SerializedFunction<E, T> sf) {
        return forName(sf, null);
    }

    @SuppressWarnings("unchecked")
    public static <E, T> Property<T> forName(SerializedFunction<E, ?> sf, Class<T> requiredType) {
        LambdaInfo info = LambdaUtils.inspect(sf);
        String alias = TableAlias.get(info.getClassName());
        return new Property<T>(alias, info.getAttributeName(),
                requiredType != null ? requiredType : (Class<T>) info.getAttributeType());
    }

}
