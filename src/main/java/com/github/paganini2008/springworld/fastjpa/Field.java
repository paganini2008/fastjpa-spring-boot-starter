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
import javax.persistence.criteria.Selection;

/**
 * 
 * Field
 *
 * @author Fred Feng
 * @since 2.0.1
 */
public interface Field<T> {

	Expression<T> toExpression(Model<?> model, CriteriaBuilder builder);

	default Column as(final String alias) {
		return new Column() {

			public Selection<?> toSelection(Model<?> model, CriteriaBuilder builder) {
				return Field.this.toExpression(model, builder).alias(alias);
			}

			public String toString() {
				return Field.this.toString();
			}
		};
	}
}
