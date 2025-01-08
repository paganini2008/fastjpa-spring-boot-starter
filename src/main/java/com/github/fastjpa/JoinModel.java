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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.StringUtils;

/**
 * 
 * @Description: JoinModel
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public class JoinModel<X, Y> implements Model<Y> {

    private final Join<X, Y> join;
    private final String alias;
    private final Metamodel metamodel;
    private final Model<X> model;

    JoinModel(Join<X, Y> join, String alias, Metamodel metamodel, Model<X> model) {
        this.join = join;
        this.alias = alias;
        this.metamodel = metamodel;
        this.model = model;

        TableAlias.put(getType(), alias);
    }

    @Override
    public Class<?> getRootType() {
        return model.getRootType();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Y> getType() {
        return (Class<Y>) join.getAttribute().getJavaType();
    }

    @Override
    public boolean isManaged(Class<?> type) {
        if (getType().equals(type)) {
            return true;
        }
        return model.isManaged(type);
    }

    @Override
    public EntityType<Y> getEntityType() {
        return metamodel.entity(getType());
    }

    @Override
    public boolean hasAttribute(String name, String attributeName) {
        return this.alias.equals(name) && StringUtils.isNotBlank(attributeName) ? true
                : model.hasAttribute(name, attributeName);
    }

    @Override
    public <T> Path<T> getAttribute(String attributeName) {
        return join.get(attributeName);
    }

    @Override
    public <T> Path<T> getAttribute(String name, String attributeName) {
        if (this.alias.equals(name) && StringUtils.isNotBlank(attributeName)) {
            return join.get(attributeName);
        }
        return model.getAttribute(name, attributeName);
    }

    @Override
    public Root<?> getRoot() {
        return model.getRoot();
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public Selection<?> getSelection(String alias) {
        if (this.alias.equals(alias)) {
            return join.alias(alias);
        }
        return model.getSelection(alias);
    }

    @Override
    public List<Selection<?>> getSelections(String alias, String[] attributeNames) {
        if (this.alias.equals(alias)) {
            List<Selection<?>> selections = new ArrayList<>();
            if (ArrayUtils.isNotEmpty(attributeNames)) {
                for (String name : attributeNames) {
                    selections.add(join.get(name));
                }
            }
        }
        return model.getSelections(alias, attributeNames);
    }

    @Override
    public List<JpaAttributeDetail> getAttributeDetails(String alias) {
        if (this.alias.equals(alias)) {
            List<JpaAttributeDetail> details = new ArrayList<JpaAttributeDetail>();
            for (SingularAttribute<? super Y, ?> attribute : getEntityType()
                    .getSingularAttributes()) {
                details.add(new JpaAttributeDetailImpl<>(attribute));
            }
            return details;
        }
        return model.getAttributeDetails(alias);
    }

    @Override
    public <Z> Model<Z> join(String attributeName, String name, Predicate on) {
        Join<Y, Z> join = this.join.join(attributeName, JoinType.INNER);
        if (on != null) {
            join.on(on);
        }
        return new JoinModel<Y, Z>(join, name, metamodel, this);
    }

    @Override
    public <Z> Model<Z> leftJoin(String attributeName, String name, Predicate on) {
        Join<Y, Z> join = this.join.join(attributeName, JoinType.LEFT);
        if (on != null) {
            join.on(on);
        }
        return new JoinModel<Y, Z>(join, name, metamodel, this);
    }

    @Override
    public <Z> Model<Z> rightJoin(String attributeName, String name, Predicate on) {
        Join<Y, Z> join = this.join.join(attributeName, JoinType.RIGHT);
        if (on != null) {
            join.on(on);
        }
        return new JoinModel<Y, Z>(join, name, metamodel, this);
    }

    @Override
    public <S> Model<S> sibling(Model<S> sibling) {
        return new SiblingModel<Y, S>(sibling, this);
    }

}
