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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Subquery;

/**
 * 
 * JpaSubQueryImpl
 *
 * @author Fred Feng
 * @version 1.0
 */
public class JpaSubQueryImpl<X, Y> implements JpaSubQuery<X, Y> {

	private final Model<X> model;
	private final Subquery<Y> query;
	private final CriteriaBuilder builder;

	JpaSubQueryImpl(Model<X> model, Subquery<Y> query, CriteriaBuilder builder) {
		this.model = model;
		this.query = query;
		this.builder = builder;
	}

	@Override
	public JpaSubQuery<X, Y> filter(Filter filter) {
		query.where(filter.toPredicate(model, builder));
		return this;
	}

	@Override
	public JpaSubQueryGroupBy<X, Y> groupBy(FieldList fieldList) {
		List<Expression<?>> paths = new ArrayList<Expression<?>>();
		for (Field<?> field : fieldList) {
			paths.add(field.toExpression(model, builder));
		}
		query.groupBy(paths);
		return new JpaSubQueryGroupByImpl<X, Y>(model, query, builder);
	}

	@Override
	public JpaSubQuery<X, Y> select(String alias, String attributeName) {
		return select(Property.forName(alias, attributeName));
	}

	@Override
	public JpaSubQuery<X, Y> select(Field<Y> field) {
		Expression<Y> expression = field.toExpression(model, builder);
		query.select(expression);
		return this;
	}

	@Override
	public JpaSubQuery<X, Y> distinct(boolean distinct) {
		query.distinct(distinct);
		return this;
	}

	@Override
	public <Z> JpaSubQuery<Z, Y> join(String attributeName, String alias, Filter on) {
		Model<Z> join = model.join(attributeName, alias, on != null ? on.toPredicate(model, builder) : null);
		return new JpaSubQueryImpl<Z, Y>(join, query, builder);
	}

	@Override
	public <Z> JpaSubQuery<Z, Y> leftJoin(String attributeName, String alias, Filter on) {
		Model<Z> join = model.leftJoin(attributeName, alias, on != null ? on.toPredicate(model, builder) : null);
		return new JpaSubQueryImpl<Z, Y>(join, query, builder);
	}

	@Override
	public <Z> JpaSubQuery<Z, Y> rightJoin(String attributeName, String alias, Filter on) {
		Model<Z> join = model.rightJoin(attributeName, alias, on != null ? on.toPredicate(model, builder) : null);
		return new JpaSubQueryImpl<Z, Y>(join, query, builder);
	}

	@Override
	public Subquery<Y> toSubquery(CriteriaBuilder builder) {
		return query;
	}

}
