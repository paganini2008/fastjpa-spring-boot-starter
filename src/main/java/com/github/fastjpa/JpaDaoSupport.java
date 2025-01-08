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
package com.github.fastjpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * 
 * @Description: JpaDaoSupport
 * @Author: Fred Feng
 * @Date: 08/01/2025
 * @Version 1.0.0
 */
public class JpaDaoSupport<E, ID> extends SimpleJpaRepository<E, ID>
        implements JpaCustomUpdate<E>, JpaCustomQuery<E> {

    public JpaDaoSupport(Class<E> entityClass, EntityManager em) {
        super(entityClass, em);
        this.em = em;
    }

    protected final EntityManager em;

    @Override
    public <T> T getSingleResult(JpaQueryCallback<T> callback) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = callback.doInJpa(builder);
        TypedQuery<T> typedQuery = em.createQuery(query);
        return typedQuery.getSingleResult();
    }

    @Override
    public <T> List<T> getResultList(JpaQueryCallback<T> callback) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = callback.doInJpa(builder);
        TypedQuery<T> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public <T> List<T> getResultList(JpaQueryCallback<T> callback, int maxResults,
            int firstResult) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = callback.doInJpa(builder);
        TypedQuery<T> typedQuery = em.createQuery(query);
        if (firstResult >= 0) {
            typedQuery.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            typedQuery.setMaxResults(maxResults);
        }
        return typedQuery.getResultList();
    }

    @Override
    public int executeUpdate(JpaDeleteCallback<E> callback) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<E> delete = callback.doInJpa(builder);
        Query query = em.createQuery(delete);
        return query.executeUpdate();
    }

    @Override
    public int executeUpdate(JpaUpdateCallback<E> callback) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaUpdate<E> update = callback.doInJpa(builder);
        Query query = em.createQuery(update);
        return query.executeUpdate();
    }

    @Override
    public JpaUpdate<E> update(Class<E> entityClass) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaUpdate<E> update = builder.createCriteriaUpdate(entityClass);
        Root<E> root = update.from(entityClass);
        return new JpaUpdateImpl<E>(new RootModel<E>(root, Model.ROOT, em.getMetamodel()), update,
                builder, this);
    }

    @Override
    public JpaDelete<E> delete(Class<E> entityClass) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<E> delete = builder.createCriteriaDelete(entityClass);
        Root<E> root = delete.from(entityClass);
        return new JpaDeleteImpl<E>(new RootModel<E>(root, Model.ROOT, em.getMetamodel()), delete,
                builder, this);
    }

    public JpaQuery<E, Tuple> query(Class<E> entityClass, String alias) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<E> root = query.from(entityClass);
        return new JpaQueryImpl<E, Tuple>(new RootModel<E>(root, alias, em.getMetamodel()), query,
                builder, this);
    }

    public <T> JpaQuery<E, T> query(Class<E> entityClass, String alias, Class<T> resultClass) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(resultClass);
        Root<E> root = query.from(entityClass);
        return new JpaQueryImpl<E, T>(new RootModel<E>(root, alias, em.getMetamodel()), query,
                builder, this);
    }

    public <T> JpaPage<E, Tuple> select(Class<E> entityClass, String alias) {
        JpaQuery<E, Tuple> query = query(entityClass, alias);
        JpaQuery<E, Long> counter = query(entityClass, alias, Long.class);
        return new JpaPageImpl<E, Tuple>(query, counter, this);
    }

    public <T> JpaPage<E, T> select(Class<E> entityClass, String alias, Class<T> resultClass) {
        JpaQuery<E, T> query = query(entityClass, alias, resultClass);
        JpaQuery<E, Long> counter = query(entityClass, alias, Long.class);
        return new JpaPageImpl<E, T>(query, counter, this);
    }

}
