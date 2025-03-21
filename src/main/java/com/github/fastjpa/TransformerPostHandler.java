
package com.github.fastjpa;

/**
 * 
 * @Description: TransformerPostHandler
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
@FunctionalInterface
public interface TransformerPostHandler<T, R> {

    void handleAfterTransformation(Model<?> model, T original, R destination);

}
