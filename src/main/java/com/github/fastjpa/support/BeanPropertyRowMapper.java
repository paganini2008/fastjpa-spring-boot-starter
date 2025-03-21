/**
 * Copyright 2017-2021 Fred Feng (paganini.fy@gmail.com)
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
 * @Date: 20/03/2025
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
