package com.github.fastjpa;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.ClassUtils;

/**
 * 
 * @Description: LambdaUtils
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public class LambdaUtils {

    private LambdaUtils() {}

    static class LambdaInfo {

        private String className;
        private String attributeName;
        private Class<?> attributeType;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public Class<?> getAttributeType() {
            return attributeType;
        }

        public void setAttributeType(Class<?> attributeType) {
            this.attributeType = attributeType;
        }

    }

    private static final Map<String, Class<?>> attributeTypeCache =
            new ConcurrentHashMap<String, Class<?>>();

    public static <E, T> LambdaInfo inspect(SerializedFunction<E, T> function) {
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
        String attributeName;
        if (methodName.startsWith("is")) {
            attributeName = methodName.substring(2);
        } else if (methodName.startsWith("get") || methodName.startsWith("set")) {
            attributeName = methodName.substring(3);
        } else {
            throw new IllegalArgumentException("Error parsing property name '" + methodName
                    + "'.  Didn't start with 'is', 'get' or 'set'.");
        }
        if (attributeName.length() == 1 || (attributeName.length() > 1
                && !Character.isUpperCase(attributeName.charAt(1)))) {
            attributeName = attributeName.substring(0, 1).toLowerCase(Locale.ENGLISH)
                    + attributeName.substring(1);
        }
        LambdaInfo lambdaInfo = new LambdaInfo();
        lambdaInfo.setClassName(className);
        lambdaInfo.setAttributeName(attributeName);

        String key = className + "." + attributeName;
        Class<?> attributeType = attributeTypeCache.get(key);
        if (attributeType == null) {
            attributeTypeCache.putIfAbsent(key, getAttributeType(className, attributeName));
            attributeType = attributeTypeCache.get(key);
        }
        lambdaInfo.setAttributeType(attributeType);
        return lambdaInfo;
    }

    private static Class<?> getAttributeType(String className, String attributeName) {
        Class<?> clz;
        try {
            clz = ClassUtils.forName(className, null);
            return clz.getDeclaredField(attributeName).getType();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {

    }

}
