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

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Description: TableAlias
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public final class TableAlias {

    private static final InheritableThreadLocal<Map<String, String>> cache =
            new InheritableThreadLocal<Map<String, String>>() {

                protected Map<String, String> initialValue() {
                    return new HashMap<>();
                }
            };

    public static void put(Type type, String alias) {
        cache.get().put(type.getTypeName(), alias);
    }

    public static String get(String typeName) {
        return cache.get().get(typeName);
    }

    public static String get(Type type) {
        return cache.get().get(type.getTypeName());
    }

    public static void clear() {
        cache.remove();
    }

}
