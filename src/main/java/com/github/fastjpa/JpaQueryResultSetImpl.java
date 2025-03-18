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

import java.util.List;

import com.github.paganini2008.devtools.jdbc.Listable;

import jakarta.persistence.criteria.CriteriaQuery;

/**
 * 
 * JpaQueryResultSetImpl
 * 
 * @author Fred Feng
 *
 * @since 2.0.1
 */
public class JpaQueryResultSetImpl<T> implements JpaQueryResultSet<T> {

    private final Model<?> model;
    private final CriteriaQuery<T> query;
    private final JpaCustomQuery<?> customQuery;

    JpaQueryResultSetImpl(Model<?> model, CriteriaQuery<T> query, JpaCustomQuery<?> customQuery) {
        this.model = model;
        this.query = query;
        this.customQuery = customQuery;
    }

    @Override
    public List<T> list(int maxResults, int firstResult) {
        return customQuery.getResultList(builder -> query, maxResults, firstResult);
    }

    @Override
    public <R> Listable<R> setTransformer(Transformer<T, R> transformer) {
        return new JpaQueryListable<T, R>(model, query, customQuery, transformer);
    }
}
