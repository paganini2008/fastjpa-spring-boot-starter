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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import com.github.paganini2008.devtools.ArrayUtils;

/**
 * 
 * JpaQueryImpl
 * 
 * @author Fred Feng
 *
 * @since 1.0
 */
public class JpaQueryImpl<E, T> implements JpaQuery<E, T> {

	JpaQueryImpl(Model<E> model, CriteriaQuery<T> query, CriteriaBuilder builder, JpaCustomQuery<?> customQuery) {
		this.model = model;
		this.query = query;
		this.builder = builder;
		this.customQuery = customQuery;
	}

	private final Model<E> model;
	private final CriteriaQuery<T> query;
	private final CriteriaBuilder builder;
	private final JpaCustomQuery<?> customQuery;

	@Override
	public JpaQuery<E, T> filter(Filter filter) {
		if (filter != null) {
			query.where(filter.toPredicate(model, builder));
		}
		return this;
	}

	@Override
	public JpaGroupBy<E, T> groupBy(FieldList fieldList) {
		List<Expression<?>> paths = new ArrayList<Expression<?>>();
		for (Field<?> field : fieldList) {
			paths.add(field.toExpression(model, builder));
		}
		query.groupBy(paths);
		return new JpaGroupByImpl<E, T>(model, query, builder, customQuery);
	}

	@Override
	public <X> JpaSubQuery<X, X> subQuery(Class<X> entityClass, String alias) {
		Subquery<X> subquery = query.subquery(entityClass);
		Root<X> root = subquery.from(entityClass);
		Model<X> model = this.model.sibling(Model.forRoot(root, alias));
		return new JpaSubQueryImpl<X, X>(model, subquery.select(root), builder);
	}

	@Override
	public <X, Y> JpaSubQuery<X, Y> subQuery(Class<X> entityClass, String alias, Class<Y> resultClass) {
		Subquery<Y> subquery = query.subquery(resultClass);
		Root<X> root = subquery.from(entityClass);
		Model<X> model = this.model.sibling(Model.forRoot(root, alias));
		return new JpaSubQueryImpl<X, Y>(model, subquery, builder);
	}

	@Override
	public JpaQueryResultSet<T> selectThis() {
		if (query.getResultType() == model.getRootType()) {
			return selectAlias(new String[0]);
		}
		return selectAlias(Model.ROOT);
	}

	@Override
	public JpaQueryResultSet<T> selectAlias(String... tableAliases) {
		if (ArrayUtils.isNotEmpty(tableAliases)) {
			List<Selection<?>> selections = new ArrayList<Selection<?>>();
			for (String tableAlias : tableAliases) {
				selections.add(model.getSelection(tableAlias));
			}
			query.multiselect(selections);
		}
		return new JpaQueryResultSetImpl<T>(model, query, customQuery);
	}

	@Override
	public JpaQueryResultSet<T> select(ColumnList columnList) {
		if (columnList != null) {
			List<Selection<?>> selections = new ArrayList<Selection<?>>();
			for (Column column : columnList) {
				selections.add(column.toSelection(model, builder));
			}
			query.multiselect(selections);
		}
		return new JpaQueryResultSetImpl<T>(model, query, customQuery);
	}

	@Override
	public T one(Column column) {
		query.multiselect(column.toSelection(model, builder));
		return customQuery.getSingleResult(builder -> query);
	}

	@Override
	public JpaQuery<E, T> sort(JpaSort... sorts) {
		if (ArrayUtils.isNotEmpty(sorts)) {
			List<Order> orders = new ArrayList<Order>();
			for (JpaSort sort : sorts) {
				orders.add(sort.toOrder(model, builder));
			}
			query.orderBy(orders);
		}
		return this;
	}

	@Override
	public JpaQuery<E, T> distinct(boolean distinct) {
		query.distinct(distinct);
		return this;
	}

	@Override
	public <X> JpaQuery<X, T> join(String attributeName, String alias, Filter on) {
		Model<X> join = model.join(attributeName, alias, on != null ? on.toPredicate(model, builder) : null);
		return new JpaQueryImpl<X, T>(join, query, builder, customQuery);
	}

	@Override
	public <X> JpaQuery<X, T> leftJoin(String attributeName, String alias, Filter on) {
		Model<X> join = model.leftJoin(attributeName, alias, on != null ? on.toPredicate(model, builder) : null);
		return new JpaQueryImpl<X, T>(join, query, builder, customQuery);
	}

	@Override
	public <X> JpaQuery<X, T> rightJoin(String attributeName, String alias, Filter on) {
		Model<X> join = model.rightJoin(attributeName, alias, on != null ? on.toPredicate(model, builder) : null);
		return new JpaQueryImpl<X, T>(join, query, builder, customQuery);
	}

	@Override
	public CriteriaQuery<T> query() {
		return query;
	}

	@Override
	public Model<E> model() {
		return model;
	}

}
