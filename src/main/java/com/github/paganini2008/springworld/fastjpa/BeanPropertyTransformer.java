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

import com.github.paganini2008.springworld.fastjpa.support.BeanReflection;

/**
 * 
 * BeanPropertyTransformer
 * 
 * @author Fred Feng
 *
 * @version 1.0
 */
public class BeanPropertyTransformer<T, R> extends AbstractTransformer<T, R> {

	public BeanPropertyTransformer(Class<R> resultClass, String[] includedProperties, TransformerPostHandler<T, R> postHandler) {
		this.beanReflection = new BeanReflection<R>(resultClass, includedProperties);
		this.postHandler = postHandler;
	}

	private final BeanReflection<R> beanReflection;
	private final TransformerPostHandler<T, R> postHandler;

	@Override
	protected void writeValue(Model<?> model, String attributeName, Class<?> attributeType, Object attributeValue, R destination) {
		beanReflection.setProperty(destination, attributeName, attributeValue);
	}

	@Override
	protected R createObject(Model<?> model, int selectionSize, T original) {
		return beanReflection.instantiateBean();
	}

	@Override
	protected final void afterTransferring(Model<?> model, T original, R destination) {
		if (postHandler != null) {
			postHandler.handleAfterTransferring(model, original, destination);
		}
	}

}
