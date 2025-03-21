package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

/**
 * 
 * @Description: Filter
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public interface Filter {

    public Predicate toPredicate(Model<?> model, CriteriaBuilder builder);

}
