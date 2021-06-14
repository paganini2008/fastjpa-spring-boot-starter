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

import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.EntityType;

/**
 * 
 * SiblingModel
 *
 * @author Fred Feng
 * @version 1.0
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
