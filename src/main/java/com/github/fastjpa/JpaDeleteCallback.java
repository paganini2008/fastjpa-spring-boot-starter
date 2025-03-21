
package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;

/**
 * 
 * @Description: JpaDeleteCallback
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaDeleteCallback<T> {

    CriteriaDelete<T> doInJpa(CriteriaBuilder builder);

}
