
package com.github.easyjpa.support;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * 
 * @Description: BeanPropertyRowMapper
 * @Author: Fred Feng
 * @Date: 20/08/2021
 * @Version 1.0.0
 */
public class BeanPropertyRowMapper<T> implements RowMapper<T> {

    public BeanPropertyRowMapper(Class<T> resultClass, String... includedProperties) {
        this.resultClass = resultClass;
        this.includedPropertyNames = includedProperties != null && includedProperties.length > 0
                ? Set.of(includedProperties)
                : Collections.emptySet();
    }

    private final Class<T> resultClass;
    private final Set<String> includedPropertyNames;

    public T mapRow(int index, Map<String, Object> data) {
        Map<String, Object> copy = new LinkedHashMap<String, Object>(data);
        BeanWrapper beanWrapper = new BeanWrapperImpl(resultClass);
        if (includedPropertyNames.size() > 0) {
            copy = copy.entrySet().stream()
                    .filter(entry -> includedPropertyNames.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        beanWrapper.setPropertyValues(copy);
        return resultClass.cast(beanWrapper.getWrappedInstance());
    }
}
