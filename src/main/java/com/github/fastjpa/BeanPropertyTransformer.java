
package com.github.fastjpa;

import com.github.fastjpa.support.BeanReflection;

/**
 * 
 * BeanPropertyTransformer
 * 
 * @author Fred Feng
 *
 * @since 2.0.1
 */
public class BeanPropertyTransformer<T, R> extends AbstractTransformer<T, R> {

    public BeanPropertyTransformer(Class<R> resultClass, String[] includedProperties,
            TransformerPostHandler<T, R> postHandler) {
        this.beanReflection = new BeanReflection<R>(resultClass, includedProperties);
        this.postHandler = postHandler;
    }

    private final BeanReflection<R> beanReflection;
    private final TransformerPostHandler<T, R> postHandler;

    @Override
    protected void writeValue(Model<?> model, String attributeName, Class<?> attributeType,
            Object attributeValue, R destination) {
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
