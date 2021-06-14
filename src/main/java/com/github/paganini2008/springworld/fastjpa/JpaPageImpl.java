/**
* Copyright 2021 Fred Feng (paganini.fy@gmail.com)

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

/**
 * 
 * JpaPageImpl
 * 
 * @author Fred Feng
 *
 * @version 1.0
 */
public class JpaPageImpl<E, T> implements JpaPage<E, T> {

	private final JpaQuery<E, T> query;
	private final JpaQuery<E, Long> counter;
	private final JpaCustomQuery<?> customQuery;

	JpaPageImpl(JpaQuery<E, T> query, JpaQuery<E, Long> counter, JpaCustomQuery<?> customQuery) {
		this.query = query;
		this.counter = counter;
		this.customQuery = customQuery;
	}

	@Override
	public JpaPage<E, T> filter(Filter filter) {
		query.filter(filter);
		counter.filter(filter);
		return this;
	}

	@Override
	public JpaPage<E, T> sort(JpaSort... sorts) {
		query.sort(sorts);
		return this;
	}

	@Override
	public JpaPageGroupBy<E, T> groupBy(FieldList fieldList) {
		JpaGroupBy<E, T> groupQuery = query.groupBy(fieldList);
		JpaGroupBy<E, Long> counterQuery = counter.groupBy(fieldList);
		return new JpaPageGroupByImpl<E, T>(groupQuery, counterQuery, customQuery);
	}

	@Override
	public JpaPageResultSet<T> selectThis() {
		query.selectThis();
		return new JpaPageResultSetImpl<T>(query.model(), query.query(), counter.query(), customQuery);
	}

	@Override
	public JpaPageResultSet<T> selectAlias(String... tableAliases) {
		query.selectAlias(tableAliases);
		return new JpaPageResultSetImpl<T>(query.model(), query.query(), counter.query(), customQuery);
	}

	@Override
	public JpaPageResultSet<T> select(ColumnList columnList) {
		query.select(columnList);
		return new JpaPageResultSetImpl<T>(query.model(), query.query(), counter.query(), customQuery);
	}

	@Override
	public <X> JpaPage<X, T> join(String attributeName, String alias, Filter on) {
		JpaQuery<X, T> joinQuery = query.join(attributeName, alias, on);
		JpaQuery<X, Long> joinCounter = counter.join(attributeName, alias, on);
		return new JpaPageImpl<X, T>(joinQuery, joinCounter, customQuery);
	}

	@Override
	public <X> JpaPage<X, T> leftJoin(String attributeName, String alias, Filter on) {
		JpaQuery<X, T> joinQuery = query.leftJoin(attributeName, alias, on);
		JpaQuery<X, Long> joinCounter = counter.leftJoin(attributeName, alias, on);
		return new JpaPageImpl<X, T>(joinQuery, joinCounter, customQuery);
	}

	@Override
	public <X> JpaPage<X, T> rightJoin(String attributeName, String alias, Filter on) {
		JpaQuery<X, T> joinQuery = query.rightJoin(attributeName, alias, on);
		JpaQuery<X, Long> joinCounter = counter.rightJoin(attributeName, alias, on);
		return new JpaPageImpl<X, T>(joinQuery, joinCounter, customQuery);
	}

}
