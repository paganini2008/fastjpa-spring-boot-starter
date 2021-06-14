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
import java.util.Arrays;

/**
 * 
 * ColumnList
 *
 * @author Fred Feng
 * @version 1.0
 */
public class ColumnList extends ArrayList<Column> {

	private static final long serialVersionUID = 1L;

	public ColumnList() {
	}

	public ColumnList(Column... columns) {
		addAll(Arrays.asList(columns));
	}

	public ColumnList addColumn(String attributeName) {
		add(Column.forName(attributeName));
		return this;
	}

	public ColumnList addColumns(String[] attributeNames) {
		if (attributeNames != null) {
			for (String attributeName : attributeNames) {
				add(Column.forName(attributeName));
			}
		}
		return this;
	}

	public ColumnList addColumn(String alias, String attributeName) {
		add(Column.forName(alias, attributeName));
		return this;
	}

	public ColumnList addColumns(String alias, String[] attributeNames) {
		if (attributeNames != null) {
			for (String attributeName : attributeNames) {
				add(Column.forName(alias, attributeName));
			}
		}
		return this;
	}

	public ColumnList addColumn(String attributeName, Class<?> requiredType) {
		add(Column.forName(attributeName, requiredType));
		return this;
	}

	public ColumnList addColumn(String alias, String attributeName, Class<?> requiredType) {
		add(Column.forName(alias, attributeName, requiredType));
		return this;
	}

	public ColumnList addColumns(Field<?>... fields) {
		if (fields != null) {
			for (Field<?> field : fields) {
				addColumn(field, field.toString());
			}
		}
		return this;
	}

	public ColumnList addColumn(Field<?> field, String alias) {
		add(field.as(alias));
		return this;
	}

}
