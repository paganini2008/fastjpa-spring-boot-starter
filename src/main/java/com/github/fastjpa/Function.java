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
package com.github.fastjpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import org.apache.commons.lang3.StringUtils;

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

    public static <T> Function<T> build(String represent, Class<T> resultClass,
            String... attributeNames) {
        Field<?>[] fields = new Field[attributeNames.length];
        int i = 0;
        for (String attributeName : attributeNames) {
            fields[i] = Property.forName(attributeName);
        }
        return new Function<T>(represent, resultClass, fields);
    }

    public static <T> Function<T> build(String represent, Class<T> resultClass,
            Field<?>... fields) {
        return new Function<T>(represent, resultClass, fields);
    }

}
