/**
 * Copyright 2017-2021 Fred Feng (paganini.fy@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

/**
 * 
 * NotFilter
 *
 * @author Fred Feng
 * @since 2.0.1
 */
public class NotFilter extends LogicalFilter {

    private final Filter filter;

    public NotFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Model<?> model, CriteriaBuilder builder) {
        return filter.toPredicate(model, builder).not();
    }

}
