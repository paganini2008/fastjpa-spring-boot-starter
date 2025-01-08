/**
 * Copyright 2017-2025 Fred Feng (paganini.fy@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.fastjpa.support.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import com.github.fastjpa.support.EntityDao;
import com.github.fastjpa.support.EntityDaoSupport;
import com.github.fastjpa.support.RowMapper;
import com.github.paganini2008.devtools.jdbc.ResultSetSlice;

/**
 * 
 * @Description: HibernateDaoSupport
 * @Author: Fred Feng
 * @Date: 08/01/2025
 * @Version 1.0.0
 */
public class HibernateDaoSupport<E, ID> extends EntityDaoSupport<E, ID>
        implements EntityDao<E, ID> {

    public HibernateDaoSupport(Class<E> entityClass, EntityManager em) {
        super(entityClass, em);
    }

    @Override
    public <T> ResultSetSlice<T> select(String sql, Object[] arguments, Class<T> resultClass) {
        return new HibernateNativeQueryResultSetSlice<T>(sql, arguments, em,
                new BeanPropertyQueryResultSetExtractor<T>(resultClass));
    }

    @Override
    public <T> ResultSetSlice<T> select(String sql, Object[] arguments, RowMapper<T> rowMapper) {
        return new HibernateNativeQueryResultSetSlice<T>(sql, arguments, em,
                new MappedQueryResultSetExtractor<T>(rowMapper));
    }

    @SuppressWarnings("unchecked")
    private static class MappedQueryResultSetExtractor<T> implements QueryResultSetExtractor<T> {

        private final RowMapper<T> rowMapper;

        MappedQueryResultSetExtractor(RowMapper<T> rowMapper) {
            this.rowMapper = rowMapper;
        }

        @Override
        public List<T> extractData(Session session, NativeQuery<?> query) {
            List<T> results = new ArrayList<T>();
            query.unwrap(NativeQueryImpl.class)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) query.getResultList();
            int index = 0;
            for (Map<String, Object> data : dataList) {
                T mappedResult = rowMapper.mapRow(index++, data);
                results.add(mappedResult);
            }
            return results;
        }

    }

    @SuppressWarnings("unchecked")
    private static class BeanPropertyQueryResultSetExtractor<T>
            implements QueryResultSetExtractor<T> {

        private final Class<T> resultClass;

        BeanPropertyQueryResultSetExtractor(Class<T> resultClass) {
            this.resultClass = resultClass;
        }

        @Override
        public List<T> extractData(Session session, NativeQuery<?> query) {
            query.unwrap(NativeQueryImpl.class)
                    .setResultTransformer(Transformers.aliasToBean(resultClass));
            return (List<T>) query.getResultList();
        }

    }

}
