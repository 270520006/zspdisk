package com.zsp.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;
@Configuration

public class ShiroConfig {
@Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@PathVariable("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean bean =new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        //        anon:无需认证就可以访问
//        authc:必须认正了才能让问
//        user:必须拥有记住我功能才能用
//        perms:拥有对某个资源的权限才能访问;
//        role:拥有某个角色权限才能访问
        Map<String,String> filterChainDefinitionMap =new HashMap<>();
        filterChainDefinitionMap.put("/user/*","authc");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        bean.setLoginUrl("/toLogin");

    return bean;
    }
    @Bean
    public DefaultWebSecurityManager securityManager(@PathVariable("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        manager.setRealm(userRealm);
        return manager;
    }
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }
}
