package com.github.fastjpa;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 
 * @Description: SerializedFunction
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public interface SerializedFunction<X, Y> extends Function<X, Y>, Serializable {

}
