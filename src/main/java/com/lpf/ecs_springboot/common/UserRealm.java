package com.lpf.ecs_springboot.common;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lpf.ecs_springboot.dao.UserDao;
import com.lpf.ecs_springboot.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 认证，实现具体验证逻辑
 *
 *   AuthorizingRealm
 *
 * •	doGetAuthenticationInfo() 方法：用来验证当前登录的用户，获取认证信息。
 •	doGetAuthorizationInfo() 方法：为当前登录成功的用户授予权限和分配角色。

 *  密码验证 ： boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info);
 *
 *

 */
@Component
public class UserRealm extends AuthorizingRealm {
    @Resource
    private com.lpf.ecs_springboot.dao.UserDao UserDao;

    /**
     * 1.认证(登录时调用) 逻辑：
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {


        //UsernamePasswordToken继承自AuthenticationToken
        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;

        //查询用户信息，根据用户名查出用户
        User user = UserDao.selectOne(new QueryWrapper<User>().eq("username", token.getUsername()));


        //这里抛出异常对应controller获取异常
        //账号不存在
        if(user == null) {
            throw new UnknownAccountException();
        }
        //账号锁定
//        if(user.getStatus() == 0){
//            throw new LockedAccountException();
//        }



        //   验证给定主体的哈希凭据  hashedCredentials
        //密码认证，这里有多个重载	  getName()  "UserRealm"
        ////sha256加密
        //String salt = RandomStringUtils.randomAlphanumeric(20);
        //	user.setSalt(salt);
        //  user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));

        //realmName=UserRealm
        // (Object principal, Object hashedCredentials, ByteSource credentialsSalt, String realmName)

        //user.getSalt()：存到数据库，添加的时候随机生成的
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()),getName());

        return info;
    }


    /**
     * 2.授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //实现授权



        return null;
    }

    //配置密码凭证匹配器，md5,md2,sha1,sha256,sha523...可选择，不同哈希加密算法实现方式
    //用户添加、用户登录判断，同一种加密算法
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.hashAlgorithmName);  //加密算法
        shaCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);        //
        super.setCredentialsMatcher(shaCredentialsMatcher);  //父类
    }
}
