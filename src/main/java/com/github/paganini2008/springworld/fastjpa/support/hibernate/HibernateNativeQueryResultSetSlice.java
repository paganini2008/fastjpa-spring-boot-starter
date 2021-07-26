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
package com.github.paganini2008.springworld.fastjpa.support.hibernate;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.query.QueryUtils;

import com.github.paganini2008.devtools.jdbc.ResultSetSlice;

/**
 * 
 * HibernateNativeQueryResultSetSlice
 *
 * @author Fred Feng
 * @since 2.0.1
 */
public class HibernateNativeQueryResultSetSlice<T> implements ResultSetSlice<T> {

	HibernateNativeQueryResultSetSlice(String sql, Object[] arguments, EntityManager em,
			QueryResultSetExtractor<T> queryResultSetExtractor) {
		this.sql = sql;
		this.arguments = arguments;
		this.queryResultSetExtractor = queryResultSetExtractor;
		this.em = em;
	}

	protected final String sql;
	protected final Object[] arguments;
	protected final EntityManager em;
	private final QueryResultSetExtractor<T> queryResultSetExtractor;

	@Override
	public List<T> list(int maxResults, int firstResult) {
		Session session = em.unwrap(Session.class);
		NativeQuery<?> query = session.createNativeQuery(sql);
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
		return queryResultSetExtractor.extractData(session, query);
	}

	@Override
	public int rowCount() {
		Session session = em.unwrap(Session.class);
		NativeQuery<?> query = session.createNativeQuery(getCountQuerySqlString(sql));
		if (arguments != null && arguments.length > 0) {
			int index = 1;
			for (Object arg : arguments) {
				query.setParameter(index++, arg);
			}
		}
		Optional<?> op = query.uniqueResultOptional();
		if (op.isPresent()) {
			Object result = op.get();
			return result instanceof Number ? ((Number) result).intValue() : 0;
		}
		return 0;
	}

	protected String getCountQuerySqlString(String sql) {
		return String.format(QueryUtils.COUNT_QUERY_STRING, "1", "(" + sql + ")");
	}

}
