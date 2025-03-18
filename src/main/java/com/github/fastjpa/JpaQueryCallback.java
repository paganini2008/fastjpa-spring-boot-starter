
package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

/**
 * 
 * @Description: JpaQueryCallback
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaQueryCallback<T> {

    CriteriaQuery<T> doInJpa(CriteriaBuilder builder);

}
