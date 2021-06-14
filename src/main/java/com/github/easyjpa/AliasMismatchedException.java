
package com.github.easyjpa;

/**
 * 
 * @Description: AliasMismatchedException
 * @Author: Fred Feng
 * @Date: 18/10/2024
 * @Version 1.0.0
 */
public class AliasMismatchedException extends IllegalArgumentException {

    private static final long serialVersionUID = 2735877872090011720L;

    public AliasMismatchedException(String alias) {
        super(alias);
    }

}
