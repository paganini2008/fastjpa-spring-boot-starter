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

/**
 * 
 * JpaUpdate
 *
 * @author Fred Feng
 * @version 1.0
 */
public interface JpaUpdate<E> extends Executable {

	JpaUpdate<E> filter(Filter filter);

	<T> JpaUpdate<E> set(String attributeName, T value);

	<T> JpaUpdate<E> set(String attributeName, String anotherAttributeName);

	<T> JpaUpdate<E> set(String attributeName, Field<T> value);

	<X> JpaSubQuery<X, X> subQuery(Class<X> entityClass);

	<X, Y> JpaSubQuery<X, Y> subQuery(Class<X> entityClass, Class<Y> resultClass);
}
