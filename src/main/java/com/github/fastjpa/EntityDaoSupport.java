package com.github.fastjpa;

import static com.github.fastjpa.Model.ROOT;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;

/**
 * 
 * @Description: EntityDaoSupport
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public abstract class EntityDaoSupport<E, ID> extends JpaDaoSupport<E, ID>
        implements EntityDao<E, ID> {

    public EntityDaoSupport(Class<E> entityClass, EntityManager em) {
        super(entityClass, em);
        this.entityClass = entityClass;
    }

    protected final Class<E> entityClass;

    @Override
    public boolean exists(Filter filter) {
        return count(filter) > 0;
    }

    @Override
    public long count(Filter filter) {
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
    public Double avg(String attributeName, Filter filter) {
        Property<Double> property = Property.forName(attributeName);
        return query(Double.class).filter(filter).one(Fields.avg(property));
    }

    @Override
    public <T extends Comparable<T>> T min(String attributeName, Filter filter,
            Class<T> requiredType) {
        Property<T> property = Property.forName(attributeName, requiredType);
        return query(requiredType).filter(filter).one(Fields.min(property));
    }

    @Override
    public <T extends Comparable<T>> T max(String attributeName, Filter filter,
            Class<T> requiredType) {
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
    public JpaPage<E, E> paginate() {
        return paginate(entityClass, ROOT, entityClass);
    }

    @Override
    public <T> JpaPage<E, T> paginate(Class<T> resultClass) {
        return paginate(entityClass, ROOT, resultClass);
    }

    @Override
    public JpaPage<E, Tuple> multiPaginate() {
        return paginate(entityClass, ROOT);
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

}
