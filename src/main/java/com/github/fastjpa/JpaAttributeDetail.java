
package com.github.fastjpa;

/**
 * 
 * @Description: JpaAttributeDetail
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaAttributeDetail {

    String getName();

    Class<?> getJavaType();

    boolean isId();

    boolean isVersion();

    boolean isOptional();

    boolean isAssociation();

    boolean isCollection();

}
