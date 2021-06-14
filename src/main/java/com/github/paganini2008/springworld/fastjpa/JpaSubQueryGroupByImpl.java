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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Subquery;

/**
 * 
 * JpaSubQueryGroupByImpl
 * 
 * @author Fred Feng
 *
 * @version 1.0
 */
public class JpaSubQueryGroupByImpl<X, Y> implements JpaSubQueryGroupBy<X, Y> {

	private final Model<X> model;
	private final Subquery<Y> query;
	private final CriteriaBuilder builder;

	JpaSubQueryGroupByImpl(Model<X> model, Subquery<Y> query, CriteriaBuilder builder) {
		this.model = model;
		this.query = query;
		this.builder = builder;
	}

	@Override
	public JpaSubQueryGroupBy<X, Y> having(Filter filter) {
		query.having(filter.toPredicate(model, builder));
		return this;
	}

	@Override
	public JpaSubQueryGroupBy<X, Y> select(String alias, String attributeName) {
		return select(Property.forName(alias, attributeName));
	}

	@Override
	public JpaSubQueryGroupBy<X, Y> select(Field<Y> field) {
		Expression<Y> expression = field.toExpression(model, builder);
		query.select(expression);
		return this;
	}

}
