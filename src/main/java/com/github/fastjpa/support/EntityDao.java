
package com.github.fastjpa.support;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;
import com.github.fastjpa.Filter;
import com.github.fastjpa.JpaDelete;
import com.github.fastjpa.JpaPage;
import com.github.fastjpa.JpaQuery;
import com.github.fastjpa.JpaUpdate;
import jakarta.persistence.Tuple;

/**
 * 
 * @Description: EntityDao
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
@NoRepositoryBean
public interface EntityDao<E, ID>
        extends JpaRepositoryImplementation<E, ID>, NativeSqlOperations<E> {

    Class<E> getEntityClass();

    boolean exists(Filter filter);

    long count(Filter filter);

    List<E> findAll(Filter filter);

    List<E> findAll(Filter filter, Sort sort);

    Page<E> findAll(Filter filter, Pageable pageable);

    Optional<E> findOne(Filter filter);

    <T extends Comparable<T>> T max(String property, Filter filter, Class<T> requiredType);

    <T extends Comparable<T>> T min(String property, Filter filter, Class<T> requiredType);

    <T extends Number> T avg(String property, Filter filter, Class<T> requiredType);

    <T extends Number> T sum(String property, Filter filter, Class<T> requiredType);

    JpaUpdate<E> update();

    JpaDelete<E> delete();

    JpaQuery<E, E> query();

    <T> JpaQuery<E, T> query(Class<T> resultClass);

    JpaQuery<E, Tuple> multiquery();

    JpaPage<E, E> select();

    <T> JpaPage<E, T> select(Class<T> resultClass);

    JpaPage<E, Tuple> multiselect();

}
