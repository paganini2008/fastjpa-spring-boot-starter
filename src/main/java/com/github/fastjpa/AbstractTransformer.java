
package com.github.fastjpa;

import java.util.List;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.Selection;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: AbstractTransformer
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
@Slf4j
public abstract class AbstractTransformer<T, R> implements Transformer<T, R> {

    @Override
    public R transfer(Model<?> model, T original) {
        List<JpaAttributeDetail> attributeDetails = model.getAttributeDetails();
        R destination = createObject(model, attributeDetails.size(), original);
        String attributeName;
        Class<?> attributeType;
        for (JpaAttributeDetail attributeDetail : attributeDetails) {
            attributeName = attributeDetail.getName();
            attributeType = attributeDetail.getJavaType();
            try {
                Object result = readValue(model, attributeName, attributeType, original);
                writeValue(model, attributeName, attributeType, result, destination);
            } catch (RuntimeException ignored) {
                if (log.isTraceEnabled()) {
                    log.trace("'{}' cannot be set value.",
                            model.getRootType().getName() + "#" + attributeName);
                }
            }
        }
        afterTransformation(model, original, destination);
        return destination;
    }

    @Override
    public R transfer(Model<?> model, List<Selection<?>> selections, T original) {
        R destination = createObject(model, selections.size(), original);
        String attributeName;
        Class<?> attributeType;
        for (Selection<?> selection : selections) {
            attributeName = selection.getAlias();
            attributeType = selection.getJavaType();

            Object result = readValue(model, attributeName, attributeType, original);
            if (model.isManaged(attributeType)) {
                Object attributeValue;
                for (JpaAttributeDetail attributeDetail : model
                        .getAttributeDetails(selection.getAlias())) {
                    attributeName = attributeDetail.getName();
                    try {
                        attributeValue = PropertyUtils.getProperty(result, attributeName);
                        writeValue(model, attributeName, attributeDetail.getJavaType(),
                                attributeValue, destination);
                    } catch (RuntimeException ignored) {
                        if (log.isTraceEnabled()) {
                            log.trace("'{}' cannot be set value.",
                                    attributeType.getName() + "#" + attributeName);
                        }
                    }
                }
            } else {
                try {
                    writeValue(model, attributeName, attributeType, result, destination);
                } catch (RuntimeException ignored) {
                    if (log.isTraceEnabled()) {
                        log.trace("'{}' cannot be set value.",
                                attributeType.getName() + "#" + attributeName);
                    }
                }
            }
        }
        afterTransformation(model, original, destination);
        return destination;
    }

    protected Object readValue(Model<?> model, String attributeName, Class<?> attributeType,
            T original) {
        if (original instanceof Tuple) {
            return ((Tuple) original).get(attributeName);
        } else {
            return PropertyUtils.getProperty(original, attributeName);
        }
    }

    protected abstract void writeValue(Model<?> model, String attributeName, Class<?> attributeType,
            Object attributeValue, R destination);

    protected abstract R createObject(Model<?> model, int selectionSize, T original);

    protected void afterTransformation(Model<?> model, T value, R object) {}

}
