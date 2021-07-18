/**
* Copyright 2018-2021 Fred Feng (paganini.fy@gmail.com)

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

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.EntityType;

/**
 * 
 * Model
 *
 * @author Fred Feng
 * @version 1.0
 */
public interface Model<X> {

	static final String ROOT = "this";

	EntityType<X> getEntityType();

	Class<?> getRootType();

	Class<X> getType();

	boolean isManaged(Class<?> type);

	Root<?> getRoot();

	String getAlias();

	<T> Path<T> getAttribute(String attributeName);

	<T> Path<T> getAttribute(String name, String attributeName);

	boolean hasAttribute(String name, String attributeName);

	Selection<?> getSelection(String alias);

	List<Selection<?>> getSelections(String alias, String[] attributeNames);

	default List<JpaAttributeDetail> getAttributeDetails() {
		return getAttributeDetails(ROOT);
	}

	List<JpaAttributeDetail> getAttributeDetails(String alias);

	<Y> Model<Y> join(String attributeName, String alias, Predicate on);

	<Y> Model<Y> leftJoin(String attributeName, String alias, Predicate on);

	<Y> Model<Y> rightJoin(String attributeName, String alias, Predicate on);

	<S> Model<S> sibling(Model<S> sibling);

	static <X> Model<X> forRoot(Root<X> root) {
		return forRoot(root, ROOT);
	}

	static <X> Model<X> forRoot(Root<X> root, String alias) {
		return new RootModel<X>(root, alias, null);
	}

}
