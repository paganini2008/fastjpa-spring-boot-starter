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

import javax.persistence.criteria.Path;

/**
 * 
 * PathUtils
 *
 * @author Fred Feng
 * @since 2.0.1
 */
public abstract class PathUtils {

	public static <X, T> Path<T> createPath(Path<X> root, String property) {
		Path<T> path = null;
		int index = property.indexOf(".");
		if (index > 0) {
			path = root.get(property.substring(0, index));
			String[] names = property.substring(index + 1).split("\\.");
			for (int i = 0; i < names.length; i++) {
				path = path.get(names[i]);
			}
		} else {
			path = root.get(property);
		}
		return path;
	}

}
