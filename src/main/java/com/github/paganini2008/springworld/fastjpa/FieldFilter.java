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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

/**
 * 
 * FieldFilter
 *
 * @author Fred Feng
 * @version 1.0
 */
public class FieldFilter<T> extends LogicalFilter {

	private final Field<T> field;
	private final PredicateBuilder<T> builder;

	FieldFilter(Field<T> field, PredicateBuilder<T> builder) {
		this.field = field;
		this.builder = builder;
	}

	public Predicate toPredicate(Model<?> model, CriteriaBuilder cb) {
		final Expression<T> expression = field.toExpression(model, cb);
		return builder.toPredicate(model, expression, cb);
	}

}
