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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.persistence.criteria.Expression;

/**
 * 
 * CaseExpression
 *
 * @author Fred Feng
 * @since 2.0.1
 */
public class CaseExpression<R> implements Field<R> {

	private final List<Field<Boolean>> conditions = new ArrayList<Field<Boolean>>();
	private final List<R> results = new ArrayList<R>();
	private final List<Field<R>> resultFields = new ArrayList<Field<R>>();
	private R defaultResult;
	private Field<R> defaultFieldResult;

	public CaseExpression<R> when(Field<Boolean> condition, R result) {
		conditions.add(condition);
		results.add(result);
		resultFields.add(null);
		return this;
	}

	public CaseExpression<R> when(Field<Boolean> condition, Field<R> result) {
		conditions.add(condition);
		results.add(null);
		resultFields.add(result);
		return this;
	}

	public CaseExpression<R> otherwise(R result) {
		this.defaultResult = result;
		return this;
	}

	public CaseExpression<R> otherwise(Field<R> otherwise) {
		this.defaultFieldResult = otherwise;
		return this;
	}

	public Expression<R> toExpression(Model<?> model, CriteriaBuilder builder) {
		Case<R> theCase = builder.selectCase();
		for (int i = 0, l = conditions.size(); i < l; i++) {
			R result = results.get(i);
			if (result != null) {
				theCase = theCase.when(conditions.get(i).toExpression(model, builder), result);
			} else if (resultFields.get(i) != null) {
				theCase = theCase.when(conditions.get(i).toExpression(model, builder), resultFields.get(i).toExpression(model, builder));
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
