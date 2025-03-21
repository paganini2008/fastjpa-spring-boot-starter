
package com.github.fastjpa.support.hibernate;

import com.github.fastjpa.EntityDao;
import com.github.fastjpa.EntityDaoSupport;
import jakarta.persistence.EntityManager;

/**
 * 
 * @Description: HibernateDaoSupport
 * @Author: Fred Feng
 * @Date: 20/03/2025
 * @Version 1.0.0
 */
public class HibernateDaoSupport<E, ID> extends EntityDaoSupport<E, ID>
        implements EntityDao<E, ID> {

    public HibernateDaoSupport(Class<E> entityClass, EntityManager em) {
        super(entityClass, em);
    }

}
