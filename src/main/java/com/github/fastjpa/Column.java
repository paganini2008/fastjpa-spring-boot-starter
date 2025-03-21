
package com.github.fastjpa;

import java.util.List;
import com.github.fastjpa.LambdaUtils.LambdaInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Selection;

/**
 * 
 * @Description: Column
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface Column {

    Selection<?> toSelection(Model<?> model, CriteriaBuilder builder);

    static Column forName(String attributeName) {
        return forName(null, attributeName);
    }

    static Column forName(String alias, String attributeName) {
        return forName(alias, attributeName, null);
    }

    static Column forName(String attributeName, Class<?> requiredType) {
        return forName(null, attributeName, requiredType);
    }

    static Column forName(String alias, String attributeName, Class<?> requiredType) {
        return Property.forName(alias, attributeName, requiredType).as(attributeName);
    }

    @SuppressWarnings("unchecked")
    static <E, T> Column forName(SerializedFunction<E, ?> sf, Class<T> requiredType) {
        LambdaInfo info = LambdaUtils.inspect(sf);
        String alias = TableAlias.get(info.getClassName());
        return new Property<T>(alias, info.getAttributeName(),
                requiredType != null ? requiredType : (Class<T>) info.getAttributeType())
                        .as(info.getAttributeName());
    }

    static Column forSubQuery(SubQueryBuilder<?> subQueryBuilder) {
        return new Column() {

            @Override
            public Selection<?> toSelection(Model<?> model, CriteriaBuilder builder) {
                return subQueryBuilder.toSubquery(builder).getSelection();
            }
        };
    }

    static Column construct(Class<?> resultClass, String alias, String[] attributeNames) {
        return new Column() {

            @Override
            public Selection<?> toSelection(Model<?> model, CriteriaBuilder builder) {
                List<Selection<?>> selections = model.getSelections(alias, attributeNames);
                return builder.construct(resultClass, selections.toArray(new Selection[0]));
            }
        };
    }

}
