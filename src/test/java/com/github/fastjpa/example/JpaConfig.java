package com.github.fastjpa.example;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.github.fastjpa.support.EntityDaoFactoryBean;

/**
 * 
 * @Description: JpaConfig
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
@EntityScan(basePackages = {"com.github.fastjpa.example.entity"})
@EnableJpaRepositories(repositoryFactoryBeanClass = EntityDaoFactoryBean.class,
        basePackages = {"com.github.fastjpa.example.dao"})
@Configuration(proxyBeanMethods = false)
public class JpaConfig {

}
