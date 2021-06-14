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
package com.github.paganini2008.springworld.fastjpa.support;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.query.QueryUtils;

import com.github.paganini2008.devtools.jdbc.ResultSetSlice;

/**
 * 
 * NativeQueryResultSetSlice
 *
 * @author Fred Feng
 * @version 1.0
 */
@SuppressWarnings("all")
public class NativeQueryResultSetSlice<E> implements ResultSetSlice<E> {

	NativeQueryResultSetSlice(String sql, Object[] arguments, Class<E> entityClass, EntityManager em) {
		this.sql = sql;
		this.arguments = arguments;
		this.em = em;
		this.entityClass = entityClass;
	}

	protected final String sql;
	protected final Object[] arguments;
	protected final EntityManager em;
	protected final Class<E> entityClass;

	public List<E> list(int maxResults, int firstResult) {
		Query query = em.createNativeQuery(sql, entityClass);
		if (arguments != null && arguments.length > 0) {
			int index = 1;
			for (Object arg : arguments) {
				query.setParameter(index++, arg);
			}
		}
		if (firstResult >= 0) {
			query.setFirstResult(firstResult);
		}
		if (maxResults > 0) {
			query.setMaxResults(maxResults);
		}
		return query.getResultList();
	}

	public int rowCount() {
		Query query = em.createNativeQuery(getCountQuerySqlString(sql));
		if (arguments != null && arguments.length > 0) {
			int index = 1;
			for (Object arg : arguments) {
				query.setParameter(index++, arg);
			}
		}
		Object result = query.getSingleResult();
		return result instanceof Number ? ((Number) result).intValue() : 0;
	}

	protected String getCountQuerySqlString(String sql) {
		return String.format(QueryUtils.COUNT_QUERY_STRING, "1", "(" + sql + ")");
	}

}
