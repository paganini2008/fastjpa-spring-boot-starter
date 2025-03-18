package com.github.fastjpa;

import java.util.List;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.EntityType;

/**
 * 
 * @Description: SiblingModel
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public class SiblingModel<X, Y> implements Model<Y> {

    private final Model<X> model;
    private final Model<Y> sibling;

    SiblingModel(Model<Y> sibling, Model<X> model) {
        this.sibling = sibling;
        this.model = model;
    }

    @Override
    public EntityType<Y> getEntityType() {
        return sibling.getEntityType();
    }

    @Override
    public Class<?> getRootType() {
        return sibling.getRootType();
    }

    @Override
    public Class<Y> getType() {
        return sibling.getType();
    }

    @Override
    public boolean isManaged(Class<?> type) {
        return getType().equals(type);
    }

    @Override
    public boolean hasAttribute(String name, String attributeName) {
        return sibling.hasAttribute(name, attributeName) ? true : model.hasAttribute(name, attributeName);
    }

    @Override
    public <T> Path<T> getAttribute(String attributeName) {
        return sibling.getAttribute(attributeName);
    }

    @Override
    public <T> Path<T> getAttribute(String name, String attributeName) {
        if (sibling.hasAttribute(name, attributeName)) {
            return sibling.getAttribute(name, attributeName);
        }
        return model.getAttribute(name, attributeName);
    }

    @Override
    public Root<?> getRoot() {
        return model.getRoot();
    }

    @Override
    public String getAlias() {
        return sibling.getAlias();
    }

    @Override
    public Selection<?> getSelection(String alias) {
        if (sibling.getAlias().equals(alias)) {
            return sibling.getSelection(alias);
        }
        return model.getSelection(alias);
    }

    @Override
    public List<Selection<?>> getSelections(String alias, String[] attributeNames) {
        if (sibling.getAlias().equals(alias)) {
            return sibling.getSelections(alias, attributeNames);
        }
        return model.getSelections(alias, attributeNames);
    }

    @Override
    public List<JpaAttributeDetail> getAttributeDetails(String name) {
        return sibling.getAttributeDetails(name);
    }

    @Override
    public <Z> Model<Z> join(String attributeName, String alias, Predicate on) {
        Join<Y, Z> join = this.sibling.getRoot().join(attributeName, JoinType.INNER);
        if (on != null) {
            join.on(on);
        }
        return new JoinModel<Y, Z>(join, alias, null, this);
    }

    @Override
    public <Z> Model<Z> leftJoin(String attributeName, String alias, Predicate on) {
        Join<Y, Z> join = this.sibling.getRoot().join(attributeName, JoinType.LEFT);
        if (on != null) {
            join.on(on);
        }
        return new JoinModel<Y, Z>(join, alias, null, this);
    }

    @Override
    public <Z> Model<Z> rightJoin(String attributeName, String alias, Predicate on) {
        Join<Y, Z> join = this.sibling.getRoot().join(attributeName, JoinType.RIGHT);
        if (on != null) {
            join.on(on);
        }
        return new JoinModel<Y, Z>(join, alias, null, this);
    }

    @Override
    public <S> Model<S> sibling(Model<S> sibling) {
        return new SiblingModel<Y, S>(sibling, this);
    }

}
