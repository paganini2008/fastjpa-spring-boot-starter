package com.github.easyjpa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import com.github.easyjpa.LambdaUtils.LambdaInfo;
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
        String attr = this.attributeName;
        if (model.isAssociatedAttribute(attr, requiredType)) {
            attr = attr + ".id";
        }
        Expression<T> expression = model.getAttribute(alias, attr);
        if (requiredType != null && supportedJavaType(requiredType)) {
            return expression.as(requiredType);
        }
        return expression;
    }

    protected boolean supportedJavaType(Class<T> requiredType) {
        if (Boolean.class.equals(requiredType) || Character.class.equals(requiredType)
                || CharSequence.class.isAssignableFrom(requiredType)
                || Number.class.isAssignableFrom(requiredType)
                || Date.class.isAssignableFrom(requiredType)
                || Calendar.class.isAssignableFrom(requiredType)
                || LocalDate.class.equals(requiredType) || LocalDateTime.class.equals(requiredType)
                || LocalTime.class.equals(requiredType)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return String.format("%s.%s", alias, attributeName);
    }

    public static <T> Property<T> forName(String alias, String attributeName) {
        return forName(alias, attributeName, null);
    }

    public static <T> Property<T> forName(String alias, String attributeName,
            Class<T> requiredType) {
        return new Property<T>(alias, attributeName, requiredType);
    }

    public static <E, T> Property<T> forName(SerializedFunction<E, T> function) {
        return forName(function, null);
    }

    @SuppressWarnings("unchecked")
    public static <E, T> Property<T> forName(SerializedFunction<E, ?> function,
            Class<T> requiredType) {
        LambdaInfo info = LambdaUtils.inspect(function);
        String alias = TableAlias.get(info.getClassName());
        return new Property<T>(alias, info.getAttributeName(),
                requiredType != null ? requiredType : (Class<T>) info.getAttributeType());
    }

}
