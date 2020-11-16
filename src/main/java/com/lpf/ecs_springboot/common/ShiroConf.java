package com.lpf.ecs_springboot.common;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConf {

    /**
     * DefaultWebSessionManager，单机环境，session交给shiro管理,重启后跳到登陆页  DefaultWebSessionManager
     * 怎么管理session
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        // 去掉shiro登录时url里的JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionValidationInterval(3600000L);

        // 设置session过期时间3600s
        sessionManager.setGlobalSessionTimeout(3600000L); // session过期时间(单位秒) 默认1800s(30min)
        return sessionManager;
    }

    /**
     * 得到SecurityManager
     *
     * @param userRealm Realm:怎么验证
     * @return
     */
    @Bean
    public SecurityManager securityManager(UserRealm userRealm, SessionManager sessionManager) {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(userRealm);

        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }


    /**
     * 4.shiroFilter过滤器，对路径过滤，进行权限验证 • 默认登录的 URL：身份认证失败会访问该 URL。 • 认证成功之后要跳转的 URL。 •
     * 权限认证失败后要跳转的 URL。 • 需要拦截或者放行的 URL：这些都放在一个 Map 中。
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

        // 产生过滤器
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();

        // 自定义securityManager
        shiroFilter.setSecurityManager(securityManager);

        // 设置默认登录的url，身份认证失败会访问该url
        shiroFilter.setLoginUrl("/NoUser");

        // 设置成功之后要跳转的链接,ajax跳转，这里不需
        // shiroFilter.setSuccessUrl("/index");

        // 设置未授权界面，权限认证失败会访问该url  /error系统里映射好了，  默认映射到   error/4xx.html  5xx.html
        shiroFilter.setUnauthorizedUrl("/errorInfo");

        // LinkedHashMap是有序的，进行顺序拦截器配置  ，根据写的顺序
        Map<String, String> filterMap = new LinkedHashMap<>();

        // 设置可以匿名访问，可以根据实际情况自己添加，放行一些静态资源等
        filterMap.put("/static/**", "anon");
        filterMap.put("/user/login", "anon");
        filterMap.put("/user/WeChat_login", "anon");
        filterMap.put("/user/getWeChatSession/**", "anon");
        filterMap.put("/user/register", "anon");
        filterMap.put("/errorInfo", "anon");
        filterMap.put("/novel/**", "anon");
        filterMap.put("/cartoon/**", "anon");

        //springboot默认
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/captcha.jpg", "anon");

        filterMap.put("/error", "anon");     //error页面放行
        filterMap.put("/WebSocket", "anon");
        filterMap.put("/WebSocket/**", "anon");
        filterMap.put("/aW5kZXgKIA==", "anon");
        filterMap.put("/Ai/**", "anon");
        filterMap.put("/static/**", "anon");
        // 设置验证访问 所有url必须通过认证才可以访问，这行代码必须放在所有权限设置的最后

        filterMap.put("/**", "authc");    //角色，权限放到数据库里

        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }


    /**
     * 管理shiro bean生命周期
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * <p>
     * LifecycleBeanPostProcessor 就是通过上述三个方法对Initializable和Destroyable这两个类的init方法和destroy方法进行内部调用来实现bean 的生命周期控制。
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /*
     *  支持shiro注解    @RequiresPermissions
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 需要开启Shiro AOP注解支持
     *
     * @param securityManager
     * @return
     * @RequiresPermissions
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
