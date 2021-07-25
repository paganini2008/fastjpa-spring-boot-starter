/**
* Copyright 2017-2021 Fred Feng (paganini.fy@gmail.com)

* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.github.paganini2008.springworld.fastjpa;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import com.github.paganini2008.devtools.jdbc.ResultSetSlice;

/**
 * 
 * JpaGroupPageResultSet
 * 
 * @author Fred Feng
 *
 * @version 1.0
 */
public class JpaGroupPageResultSet<T> implements JpaPageResultSet<T> {

	private final Model<?> model;
	private final CriteriaQuery<T> query;
	private final CriteriaQuery<Long> counter;
	private final JpaCustomQuery<?> customQuery;

	JpaGroupPageResultSet(Model<?> model, CriteriaQuery<T> query, CriteriaQuery<Long> counter, JpaCustomQuery<?> customQuery) {
		this.model = model;
		this.query = query;
		this.counter = counter;
		this.customQuery = customQuery;
	}

	@Override
	public List<T> list(int maxResults, int firstResult) {
		return customQuery.getResultList(builder -> query, maxResults, firstResult);
	}

	@Override
	public int rowCount() {
		List<Long> list = customQuery.getResultList(builder -> {
			counter.select(builder.count(builder.toInteger(builder.literal(1))));
			return counter;
		});
		return list != null ? list.size() : 0;
	}

	@Override
	public <R> ResultSetSlice<R> setTransformer(Transformer<T, R> transformer) {
		return new JpaGroupPageResultSetSlice<T, R>(model, query, counter, customQuery, transformer);
	}

}
