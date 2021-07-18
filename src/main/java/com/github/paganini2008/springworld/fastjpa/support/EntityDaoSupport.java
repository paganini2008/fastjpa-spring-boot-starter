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
package com.github.paganini2008.springworld.fastjpa.support;

import static com.github.paganini2008.springworld.fastjpa.Model.ROOT;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.github.paganini2008.devtools.converter.ConvertUtils;
import com.github.paganini2008.devtools.jdbc.ResultSetSlice;
import com.github.paganini2008.springworld.fastjpa.Fields;
import com.github.paganini2008.springworld.fastjpa.Filter;
import com.github.paganini2008.springworld.fastjpa.JpaDaoSupport;
import com.github.paganini2008.springworld.fastjpa.JpaDelete;
import com.github.paganini2008.springworld.fastjpa.JpaPage;
import com.github.paganini2008.springworld.fastjpa.JpaQuery;
import com.github.paganini2008.springworld.fastjpa.JpaUpdate;
import com.github.paganini2008.springworld.fastjpa.Model;
import com.github.paganini2008.springworld.fastjpa.Property;

/**
 * 
 * EntityDaoSupport
 * 
 * @author Fred Feng
 *
 * @version 1.0
 */
public abstract class EntityDaoSupport<E, ID> extends JpaDaoSupport<E, ID> implements EntityDao<E, ID> {

	public EntityDaoSupport(Class<E> entityClass, EntityManager em) {
		super(entityClass, em);
		this.entityClass = entityClass;
	}

	protected final Class<E> entityClass;

	@Override
	public ResultSetSlice<E> select(String sql, Object[] arguments) {
		return new NativeQueryResultSetSlice<E>(sql, arguments, entityClass, em);
	}

	@Override
	public <T> T getSingleResult(String sql, Object[] arguments, Class<T> requiredType) {
		return execute(sql, arguments, query -> {
			Object result = query.getSingleResult();
			try {
				return result != null ? requiredType.cast(result) : null;
			} catch (ClassCastException e) {
				return (T) ConvertUtils.convertValue(result, requiredType);
			}
		});
	}

	@Override
	public int executeUpdate(String sql, Object[] arguments) {
		return execute(sql, arguments, query -> {
			return query.executeUpdate();
		});
	}

	@Override
	public <T> T execute(String sql, Object[] arguments, ResultSetExtractor<T> extractor) {
		Query query = em.createNativeQuery(sql);
		if (arguments != null && arguments.length > 0) {
			int index = 1;
			for (Object arg : arguments) {
				query.setParameter(index++, arg);
			}
		}
		return extractor.extractData(query);
	}

	@Override
	public boolean exists(Filter filter) {
		return count(filter) > 0;
	}

	@Override
	public long count(final Filter filter) {
		return count((root, query, builder) -> {
			return filter.toPredicate(Model.forRoot(root), builder);
		});
	}

	@Override
	public List<E> findAll(Filter filter) {
		return findAll((root, query, builder) -> {
			return filter.toPredicate(Model.forRoot(root), builder);
		});
	}

	@Override
	public List<E> findAll(Filter filter, Sort sort) {
		return findAll((root, query, builder) -> {
			return filter.toPredicate(Model.forRoot(root), builder);
		}, sort);
	}

	@Override
	public Page<E> findAll(Filter filter, Pageable pageable) {
		return findAll((root, query, builder) -> {
			return filter.toPredicate(Model.forRoot(root), builder);
		}, pageable);
	}

	@Override
	public Optional<E> findOne(Filter filter) {
		return findOne((root, query, builder) -> {
			return filter.toPredicate(Model.forRoot(root), builder);
		});
	}

	@Override
	public <T extends Number> T sum(String attributeName, Filter filter, Class<T> requiredType) {
		Property<T> property = Property.forName(attributeName, requiredType);
		return query(requiredType).filter(filter).one(Fields.sum(property));
	}

	@Override
	public <T extends Number> T avg(String attributeName, Filter filter, Class<T> requiredType) {
		Property<T> property = Property.forName(attributeName, requiredType);
		return query(requiredType).filter(filter).one(Fields.avg(property));
	}

	@Override
	public <T extends Comparable<T>> T min(String attributeName, Filter filter, Class<T> requiredType) {
		Property<T> property = Property.forName(attributeName, requiredType);
		return query(requiredType).filter(filter).one(Fields.min(property));
	}

	@Override
	public <T extends Comparable<T>> T max(String attributeName, Filter filter, Class<T> requiredType) {
		Property<T> property = Property.forName(attributeName, requiredType);
		return query(requiredType).filter(filter).one(Fields.max(property));
	}

	@Override
	public JpaDelete<E> delete() {
		return delete(entityClass);
	}

	@Override
	public JpaUpdate<E> update() {
		return update(entityClass);
	}

	@Override
	public JpaQuery<E, E> query() {
		return query(entityClass, ROOT, entityClass);
	}

	@Override
	public <T> JpaQuery<E, T> query(Class<T> resultClass) {
		return query(entityClass, ROOT, resultClass);
	}

	@Override
	public JpaQuery<E, Tuple> multiquery() {
		return query(entityClass, ROOT);
	}

	@Override
	public JpaPage<E, E> select() {
		return select(entityClass, ROOT, entityClass);
	}

	@Override
	public <T> JpaPage<E, T> select(Class<T> resultClass) {
		return select(entityClass, ROOT, resultClass);
	}

	@Override
	public JpaPage<E, Tuple> multiselect() {
		return select(entityClass, ROOT);
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

}
