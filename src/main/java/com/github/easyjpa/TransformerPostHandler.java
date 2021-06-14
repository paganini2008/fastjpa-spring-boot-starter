
package com.github.easyjpa;

/**
 * 
 * @Description: TransformerPostHandler
 * @Author: Fred Feng
 * @Date: 18/10/2024
 * @Version 1.0.0
 */
@FunctionalInterface
public interface TransformerPostHandler<T, R> {

    void handleAfterTransformation(Model<?> model, T original, R destination);

}
