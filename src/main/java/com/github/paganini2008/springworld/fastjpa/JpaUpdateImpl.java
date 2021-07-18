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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * 
 * JpaUpdateImpl
 *
 * @author Fred Feng
 * @version 1.0
 */
public class JpaUpdateImpl<E> implements JpaUpdate<E> {

	private final Model<E> model;
	private final CriteriaUpdate<E> update;
	private final CriteriaBuilder builder;
	private final JpaCustomUpdate<E> customUpdate;

	JpaUpdateImpl(Model<E> model, CriteriaUpdate<E> update, CriteriaBuilder builder, JpaCustomUpdate<E> customUpdate) {
		this.model = model;
		this.update = update;
		this.builder = builder;
		this.customUpdate = customUpdate;
	}

	@Override
	public JpaUpdate<E> filter(Filter filter) {
		if (filter != null) {
			update.where(filter.toPredicate(model, builder));
		}
		return this;
	}

	@Override
	public <T> JpaUpdate<E> set(String attributeName, T value) {
		Path<T> path = model.getAttribute(attributeName);
		update.set(path, value);
		return this;
	}

	@Override
	public JpaUpdate<E> set(String attributeName, String anotherAttributeName) {
		return set(attributeName, Property.forName(anotherAttributeName));
	}

	@Override
	public <T> JpaUpdate<E> set(String attributeName, Field<T> value) {
		Path<T> path = model.getAttribute(attributeName);
		update.set(path, value.toExpression(model, builder));
		return this;
	}

	@Override
	public <X> JpaSubQuery<X, X> subQuery(Class<X> entityClass) {
		Subquery<X> subquery = update.subquery(entityClass);
		Root<X> root = subquery.from(entityClass);
		return new JpaSubQueryImpl<X, X>(Model.forRoot(root), subquery, builder);
	}

	@Override
	public <X, Y> JpaSubQuery<X, Y> subQuery(Class<X> entityClass, Class<Y> resultClass) {
		Subquery<Y> subquery = update.subquery(resultClass);
		Root<X> root = subquery.from(entityClass);
		return new JpaSubQueryImpl<X, Y>(Model.forRoot(root), subquery, builder);
	}

	@Override
	public int execute() {
		return customUpdate.executeUpdate((CriteriaBuilder builder) -> update);
	}
}
