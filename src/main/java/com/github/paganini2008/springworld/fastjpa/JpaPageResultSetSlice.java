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

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Selection;

import com.github.paganini2008.devtools.jdbc.ResultSetSlice;

/**
 * 
 * JpaPageResultSetSlice
 * 
 * @author Fred Feng
 *
 * @since 2.0.1
 */
public class JpaPageResultSetSlice<T, R> implements ResultSetSlice<R> {

	private final Model<?> model;
	private final CriteriaQuery<T> query;
	private final CriteriaQuery<Long> counter;
	private final JpaCustomQuery<?> customQuery;
	private final Transformer<T, R> transformer;

	JpaPageResultSetSlice(Model<?> model, CriteriaQuery<T> query, CriteriaQuery<Long> counter, JpaCustomQuery<?> customQuery,
			Transformer<T, R> transformer) {
		this.model = model;
		this.query = query;
		this.counter = counter;
		this.customQuery = customQuery;
		this.transformer = transformer;
	}

	@Override
	public int rowCount() {
		Long result = customQuery.getSingleResult(builder -> {
			counter.select(builder.count(builder.toInteger(builder.literal(1))));
			return counter;
		});
		return result != null ? result.intValue() : 0;
	}

	@Override
	public List<R> list(int maxResults, int firstResult) {
		List<R> results = new ArrayList<R>();
		List<T> list = customQuery.getResultList(builder -> query, maxResults, firstResult);
		if (query.getResultType() == model.getRootType()) {
			for (T original : list) {
				R data = transformer.transfer(model, original);
				results.add(data);
			}
		} else {
			List<Selection<?>> selections = query.getSelection().getCompoundSelectionItems();
			for (T original : list) {
				R data = transformer.transfer(model, selections, original);
				results.add(data);
			}
		}
		return results;
	}

}
