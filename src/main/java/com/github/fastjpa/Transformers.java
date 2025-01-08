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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.Selection;
import com.github.paganini2008.devtools.collection.CaseInsensitiveMap;

/**
 * 
 * @Description: Transformers
 * @Author: Fred Feng
 * @Date: 08/01/2025
 * @Version 1.0.0
 */
public abstract class Transformers {

    public static <T> Transformer<T, T> noop() {
        return new NoopTransformer<T>();
    }

    public static <T> Transformer<T, Map<String, Object>> asMap() {
        return new MapTransformer<T>(false);
    }

    public static <T> Transformer<T, Map<String, Object>> asCaseInsensitiveMap() {
        return new MapTransformer<T>(true);
    }

    public static <T> Transformer<T, List<Object>> asList() {
        return new ListTransformer<T>();
    }

    public static <T, R> Transformer<T, R> asBean(Class<R> resultClass) {
        return asBean(resultClass, null);
    }

    public static <T, R> Transformer<T, R> asBean(Class<R> resultClass,
            TransformerPostHandler<T, R> postHandler) {
        return asBean(resultClass, new String[0], postHandler);
    }

    public static <T, R> Transformer<T, R> asBean(Class<R> resultClass, String[] includedProperties,
            TransformerPostHandler<T, R> postHandler) {
        return new BeanPropertyTransformer<T, R>(resultClass, includedProperties, postHandler);
    }

    /**
     * 
     * NoopTransformer
     * 
     * @author Fred Feng
     *
     * @since 2.0.1
     */
    public static class NoopTransformer<T> implements Transformer<T, T> {

        NoopTransformer() {}

        @Override
        public T transfer(Model<?> model, T original) {
            return original;
        }

        @Override
        public T transfer(Model<?> model, List<Selection<?>> selections, T original) {
            return original;
        }

    }

    /**
     * 
     * MapTransformer
     * 
     * @author Fred Feng
     *
     * @since 2.0.1
     */
    public static class MapTransformer<T> extends AbstractTransformer<T, Map<String, Object>> {

        MapTransformer(boolean caseInsensitive) {
            this.caseInsensitive = caseInsensitive;
        }

        private final boolean caseInsensitive;

        @Override
        protected Map<String, Object> createObject(Model<?> model, int selectionSize, T original) {
            return caseInsensitive ? new CaseInsensitiveMap<>(new LinkedHashMap<>(selectionSize))
                    : new LinkedHashMap<>(selectionSize);
        }

        @Override
        protected void writeValue(Model<?> model, String attributeName, Class<?> attributeType,
                Object attributeValue, Map<String, Object> data) {
            data.put(attributeName, attributeValue);
        }

    }

    /**
     * 
     * ListTransformer
     * 
     * @author Fred Feng
     *
     * @since 2.0.1
     */
    public static class ListTransformer<T> extends AbstractTransformer<T, List<Object>> {

        ListTransformer() {}

        @Override
        protected List<Object> createObject(Model<?> model, int selectionSize, T original) {
            return new ArrayList<Object>(selectionSize);
        }

        @Override
        protected void writeValue(Model<?> model, String attributeName, Class<?> attributeType,
                Object attributeValue, List<Object> dataList) {
            dataList.add(attributeValue);
        }

    }

}
