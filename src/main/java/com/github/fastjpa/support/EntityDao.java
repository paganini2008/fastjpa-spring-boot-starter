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
package com.github.fastjpa.support;

import java.util.List;
import java.util.Optional;
import javax.persistence.Tuple;
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

/**
 * 
 * @Description: EntityDao
 * @Author: Fred Feng
 * @Date: 08/01/2025
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
