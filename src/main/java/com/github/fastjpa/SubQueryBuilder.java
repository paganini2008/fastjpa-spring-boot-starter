package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Subquery;

/**
 * 
 * @Description: SubQueryBuilder
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public interface SubQueryBuilder<T> {

    Subquery<T> toSubquery(CriteriaBuilder builder);

}
