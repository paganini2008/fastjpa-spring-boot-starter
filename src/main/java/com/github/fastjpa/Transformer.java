
package com.github.fastjpa;

import java.util.List;
import jakarta.persistence.criteria.Selection;

/**
 * 
 * @Description: Transformer
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface Transformer<T, R> {

    R transfer(Model<?> model, T original);

    R transfer(Model<?> model, List<Selection<?>> selections, T original);

}
