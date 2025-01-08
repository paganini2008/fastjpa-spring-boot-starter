/**
 * Copyright 2017-2025 Fred Feng (paganini.fy@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.fastjpa.support;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import com.github.paganini2008.devtools.beans.PropertyUtils;
import com.github.paganini2008.devtools.reflection.ConstructorUtils;
import com.github.paganini2008.devtools.reflection.FieldUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: BeanReflection
 * @Author: Fred Feng
 * @Date: 08/01/2025
 * @Version 1.0.0
 */
@Slf4j
public final class BeanReflection<T> {

    public BeanReflection(Class<T> requiredType, String[] includedProperties) {
        this.requiredType = requiredType;
        if (includedProperties != null && includedProperties.length > 0) {
            includedPropertyNames = new HashSet<String>(Arrays.asList(includedProperties));
        }
    }

    private final Class<T> requiredType;
    private Set<String> includedPropertyNames;

    public void setProperty(T object, String attributeName, Object attributeValue) {
        final String propertyName = attributeName;
        if (hasPropertyValue(propertyName)) {
            try {
                PropertyUtils.setProperty(object, propertyName, attributeValue);
            } catch (Exception e) {
                try {
                    FieldUtils.writeField(object, propertyName, attributeValue);
                } catch (Exception ignored) {
                    if (log.isTraceEnabled()) {
                        log.trace("Attribute '{}' cannot be assigned value.",
                                requiredType.getName() + "#" + propertyName);
                    }
                }
            }
        }

    }

    public T instantiateBean() {
        try {
            return ConstructorUtils.invokeConstructor(requiredType, (Object[]) null);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private boolean hasPropertyValue(String propertyName) {
        return includedPropertyNames == null || includedPropertyNames.contains(propertyName);
    }

}
