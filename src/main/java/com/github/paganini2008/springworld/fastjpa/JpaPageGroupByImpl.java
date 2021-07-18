/**
* Copyright 2018-2021 Fred Feng (paganini.fy@gmail.com)

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

import javax.persistence.criteria.CriteriaQuery;

/**
 * 
 * JpaPageGroupByImpl
 * 
 * @author Fred Feng
 *
 * @version 1.0
 */
public class JpaPageGroupByImpl<E, T> implements JpaPageGroupBy<E, T> {

	private final JpaGroupBy<E, T> query;
	private final JpaGroupBy<E, Long> counter;
	private final JpaCustomQuery<?> customQuery;

	JpaPageGroupByImpl(JpaGroupBy<E, T> query, JpaGroupBy<E, Long> counter, JpaCustomQuery<?> customQuery) {
		this.query = query;
		this.counter = counter;
		this.customQuery = customQuery;
	}

	@Override
	public JpaPageGroupBy<E, T> having(Filter expression) {
		query.having(expression);
		counter.having(expression);
		return this;

	}

	@Override
	public JpaPageResultSet<T> select(ColumnList columnList) {
		query.select(columnList);
		return new JpaGroupPageResultSet<T>(query.model(), query.query(), counter.query(), customQuery);
	}

	@Override
	public JpaPageGroupBy<E, T> sort(JpaSort... sorts) {
		query.sort(sorts);
		return this;
	}

	@Override
	public CriteriaQuery<T> query() {
		return query.query();
	}

	@Override
	public Model<E> model() {
		return query.model();
	}

}
