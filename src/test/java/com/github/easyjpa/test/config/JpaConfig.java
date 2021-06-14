package com.github.easyjpa.test.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.github.easyjpa.EntityDaoFactoryBean;

/**
 * 
 * Jpa Configuration Bean
 * 
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
@EntityScan(basePackages = {"com.github.easyjpa.test.entity"})
@EnableJpaRepositories(repositoryFactoryBeanClass = EntityDaoFactoryBean.class,
        basePackages = {"com.github.easyjpa.test.dao"})
@Configuration(proxyBeanMethods = false)
public class JpaConfig {

}
