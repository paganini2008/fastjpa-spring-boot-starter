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

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Locale;
import lombok.Data;
import lombok.experimental.UtilityClass;

/**
 * 
 * @Description: LambdaUtils
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
@UtilityClass
public class LambdaUtils {

    @Data
    static class LambdaInfo {

        private String className;
        private String attributeName;

    }

    public <X> LambdaInfo inspect(SerializedFunction<X, ?> function) {
        SerializedLambda object;
        try {
            Method method = function.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            object = (SerializedLambda) method.invoke(function);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        String className = object.getImplClass().replace('/', '.');
        String methodName = object.getImplMethodName();
        String propertyName;
        if (methodName.startsWith("is")) {
            propertyName = methodName.substring(2);
        } else if (methodName.startsWith("get") || methodName.startsWith("set")) {
            propertyName = methodName.substring(3);
        } else {
            throw new IllegalArgumentException("Error parsing property name '" + methodName
                    + "'.  Didn't start with 'is', 'get' or 'set'.");
        }
        if (propertyName.length() == 1
                || (propertyName.length() > 1 && !Character.isUpperCase(propertyName.charAt(1)))) {
            propertyName = propertyName.substring(0, 1).toLowerCase(Locale.ENGLISH)
                    + propertyName.substring(1);
        }
        LambdaInfo lambdaInfo = new LambdaInfo();
        lambdaInfo.setClassName(className);
        lambdaInfo.setAttributeName(propertyName);
        return lambdaInfo;
    }

}
