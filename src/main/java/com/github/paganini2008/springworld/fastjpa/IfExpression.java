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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.SimpleCase;
import javax.persistence.criteria.Expression;

/**
 * 
 * IfExpression
 * 
 * @author Fred Feng
 *
 * @since 1.0
 */
public class IfExpression<T, R> implements Field<R> {

	private final Field<T> field;
	private final List<T> conditions = new ArrayList<T>();
	private final List<R> results = new ArrayList<R>();
	private final List<Field<R>> resultFields = new ArrayList<Field<R>>();
	private R defaultResult;
	private Field<R> defaultFieldResult;

	public IfExpression(String attributeName) {
		this(Property.forName(attributeName));
	}

	public IfExpression(String alias, String attributeName) {
		this(Property.forName(alias, attributeName));
	}

	public IfExpression(Field<T> field) {
		this.field = field;
	}

	public IfExpression<T, R> when(T condition, R result) {
		conditions.add(condition);
		results.add(result);
		resultFields.add(null);
		return this;
	}

	public IfExpression<T, R> when(T condition, Field<R> result) {
		conditions.add(condition);
		results.add(null);
		resultFields.add(result);
		return this;
	}

	public IfExpression<T, R> otherwise(R result) {
		this.defaultResult = result;
		return this;
	}

	public IfExpression<T, R> otherwise(Field<R> otherwise) {
		this.defaultFieldResult = otherwise;
		return this;
	}

	public Expression<R> toExpression(Model<?> model, CriteriaBuilder builder) {
		SimpleCase<T, R> theCase = builder.selectCase(field.toExpression(model, builder));
		for (int i = 0, l = conditions.size(); i < l; i++) {
			R result = results.get(i);
			if (result != null) {
				theCase = theCase.when(conditions.get(i), result);
			} else if (resultFields.get(i) != null) {
				theCase = theCase.when(conditions.get(i), resultFields.get(i).toExpression(model, builder));
			}
		}
		if (defaultResult != null) {
			return theCase.otherwise(defaultResult);
		} else if (defaultFieldResult != null) {
			return theCase.otherwise(defaultFieldResult.toExpression(model, builder));
		}
		throw new UnsupportedOperationException(toString());
	}

}
