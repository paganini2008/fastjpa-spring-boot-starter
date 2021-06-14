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
package com.github.paganini2008.springworld.fastjpa;

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
 * RootModel
 *
 * @author Fred Feng
 * @version 1.0
 */
public class RootModel<X> implements Model<X> {

	private final Root<X> root;
	private final String alias;
	private final Metamodel metamodel;

	RootModel(Root<X> root, String alias, Metamodel metamodel) {
		this.root = root;
		this.alias = alias;
		this.metamodel = metamodel;
	}

	@Override
	public boolean hasAttribute(String name, String attributeName) {
		return this.alias.equals(name) && StringUtils.isNotBlank(attributeName);
	}

	@Override
	public <T> Path<T> getAttribute(String attributeName) {
		return getAttribute(ROOT, attributeName);
	}

	@Override
	public <T> Path<T> getAttribute(String name, String attributeName) {
		if (hasAttribute(name, attributeName)) {
			return PathUtils.createPath(root, attributeName);
		}
		throw new PathMismatchedException(name, attributeName);
	}

	@Override
	public Class<?> getRootType() {
		return getType();
	}

	@Override
	public Class<X> getType() {
		return root.getModel().getBindableJavaType();
	}

	@Override
	public EntityType<X> getEntityType() {
		return root != null ? root.getModel() : null;
	}

	@Override
	public boolean isManaged(Class<?> type) {
		return getType().equals(type);
	}

	@Override
	public <Y> Model<Y> join(String attributeName, String alias, Predicate on) {
		Join<X, Y> join = root.join(attributeName, JoinType.INNER);
		if (on != null) {
			join.on(on);
		}
		return new JoinModel<X, Y>(join, alias, metamodel, this);
	}

	@Override
	public <Y> Model<Y> leftJoin(String attributeName, String alias, Predicate on) {
		Join<X, Y> join = root.join(attributeName, JoinType.LEFT);
		if (on != null) {
			join.on(on);
		}
		return new JoinModel<X, Y>(join, alias, metamodel, this);
	}

	@Override
	public <Y> Model<Y> rightJoin(String attributeName, String alias, Predicate on) {
		Join<X, Y> join = root.join(attributeName, JoinType.RIGHT);
		if (on != null) {
			join.on(on);
		}
		return new JoinModel<X, Y>(join, alias, metamodel, this);
	}

	@Override
	public Root<X> getRoot() {
		return root;
	}

	@Override
	public String getAlias() {
		return alias;
	}

	@Override
	public Selection<?> getSelection(String alias) {
		if (!this.alias.equals(alias)) {
			throw new AliasMismatchedException(alias);
		}
		return root.alias(alias);
	}

	@Override
	public List<Selection<?>> getSelections(String alias, String[] attributeNames) {
		if (!this.alias.equals(alias)) {
			throw new AliasMismatchedException(alias);
		}
		List<Selection<?>> selections = new ArrayList<>();
		if (ArrayUtils.isNotEmpty(attributeNames)) {
			for (String name : attributeNames) {
				selections.add(root.get(name));
			}
		}
		return selections;
	}

	@Override
	public List<JpaAttributeDetail> getAttributeDetails(String name) {
		if (!this.alias.equals(name)) {
			throw new AliasMismatchedException(name);
		}
		List<JpaAttributeDetail> details = new ArrayList<JpaAttributeDetail>();
		for (SingularAttribute<? super X, ?> attribute : root.getModel().getSingularAttributes()) {
			details.add(new JpaAttributeDetailImpl<>(attribute));
		}
		return details;
	}

	@Override
	public <S> Model<S> sibling(Model<S> sibling) {
		return new SiblingModel<X, S>(sibling, this);
	}

}
