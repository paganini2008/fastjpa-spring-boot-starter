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
package com.github.paganini2008.springworld.fastjpa.support;

import java.util.Map;

/**
 * 
 * BeanPropertyRowMapper
 *
 * @author Fred Feng
 * @version 1.0
 */
public class BeanPropertyRowMapper<T> implements RowMapper<T> {

	private final BeanReflection<T> beanReflection;

	public BeanPropertyRowMapper(Class<T> resultClass, String... includedProperties) {
		this.beanReflection = new BeanReflection<T>(resultClass, includedProperties);
	}

	public T mapRow(int index, Map<String, Object> data) {
		T instance = beanReflection.instantiateBean();
		for (String propertyName : data.keySet()) {
			beanReflection.setProperty(instance, propertyName, data.get(propertyName));
		}
		return instance;
	}
}
