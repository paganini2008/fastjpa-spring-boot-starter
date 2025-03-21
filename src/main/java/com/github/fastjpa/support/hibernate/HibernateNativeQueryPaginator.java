
package com.github.fastjpa.support.hibernate;

import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.query.QueryUtils;
import com.github.fastjpa.page.PageableQuery;
import jakarta.persistence.EntityManager;

/**
 * 
 * @Description: HibernateNativeQueryPaginator
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public class HibernateNativeQueryPaginator<T> implements PageableQuery<T> {

    HibernateNativeQueryPaginator(String sql, Object[] arguments, EntityManager em,
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
    public List<T> list(int maxResults, long firstResult) {
        Session session = em.unwrap(Session.class);
        NativeQuery<?> query = session.createNativeQuery(sql);
        if (arguments != null && arguments.length > 0) {
            int index = 1;
            for (Object arg : arguments) {
                query.setParameter(index++, arg);
            }
        }
        if (firstResult >= 0) {
            query.setFirstResult((int) firstResult);
        }
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
        return queryResultSetExtractor.extractData(session, query);
    }

    @Override
    public long rowCount() {
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
