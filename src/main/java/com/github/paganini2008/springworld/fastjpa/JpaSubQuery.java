/**
* Copyright 2017-2021 Fred Feng (paganini.fy@gmail.com)

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

/**
 * 
 * JpaSubQuery
 *
 * @author Fred Feng
 * @version 1.0
 */
public interface JpaSubQuery<X, Y> extends SubQueryBuilder<Y> {

	JpaSubQuery<X, Y> filter(Filter filter);

	default JpaSubQueryGroupBy<X, Y> groupBy(String alias, String... attributeNames) {
		return groupBy(new FieldList().addFields(alias, attributeNames));
	}

	default JpaSubQueryGroupBy<X, Y> groupBy(Field<?>... fields) {
		return groupBy(new FieldList(fields));
	}

	JpaSubQueryGroupBy<X, Y> groupBy(FieldList fieldList);

	default JpaSubQuery<X, Y> select(String attributeName) {
		return select(null, attributeName);
	}

	JpaSubQuery<X, Y> select(String alias, String attributeName);

	JpaSubQuery<X, Y> select(Field<Y> field);

	JpaSubQuery<X, Y> distinct(boolean distinct);

	default <Z> JpaSubQuery<Z, Y> join(String attributeName, String alias) {
		return join(attributeName, alias, null);
	}

	default <Z> JpaSubQuery<Z, Y> leftJoin(String attributeName, String alias) {
		return leftJoin(attributeName, alias, null);
	}

	default <Z> JpaSubQuery<Z, Y> rightJoin(String attributeName, String alias) {
		return rightJoin(attributeName, alias, null);
	}

	<Z> JpaSubQuery<Z, Y> join(String attributeName, String alias, Filter on);

	<Z> JpaSubQuery<Z, Y> leftJoin(String attributeName, String alias, Filter on);

	<Z> JpaSubQuery<Z, Y> rightJoin(String attributeName, String alias, Filter on);

}
