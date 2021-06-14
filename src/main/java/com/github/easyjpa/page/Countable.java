package com.github.easyjpa.page;

/**
 * @Description: Countable
 * @Author: Fred Feng
 * @Date: 08/03/2023
 * @Version 1.0.0
 */
public interface Countable {

    default long rowCount() throws Exception {
        return Integer.MAX_VALUE;
    }
}
