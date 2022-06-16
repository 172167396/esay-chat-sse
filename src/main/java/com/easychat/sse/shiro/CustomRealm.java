package com.easychat.sse.shiro;

import com.easychat.sse.model.dto.IdName;
import com.easychat.sse.model.dto.IdTitle;
import com.easychat.sse.model.entity.UserEntity;
import com.easychat.sse.service.UserService;
import com.easychat.sse.utils.ContextHolder;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

import static com.easychat.sse.constant.Constant.ERROR_ACCOUNT_PASSWORD;

public class CustomRealm extends AuthorizingRealm {


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        new SimpleAuthorizationInfo();
        SimpleAuthorizationInfo simpleAuthorizationInfo;
        UserEntity userInfo = (UserEntity) principalCollection.getPrimaryPrincipal();
        //根据用户名查询出用户角色和权限，并交给shiro管理。实际应用中用户角色和权限从数据库获取
        simpleAuthorizationInfo = buildUserAdminRolePermission();
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        UserService userService = ContextHolder.getBean(UserService.class);
        UserEntity user = userService.getUserByAccount(token.getUsername());
        if (user == null) {
            throw new UnknownAccountException(ERROR_ACCOUNT_PASSWORD);
        }
        if (!new String(token.getPassword()).equals(user.getPassword())) {
            throw new AuthenticationException(ERROR_ACCOUNT_PASSWORD);
        }
        //查询用户好友分组
        List<IdTitle> groups = userService.getUserGroups(user.getId());
        user.setGroups(groups);
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

    private SimpleAuthorizationInfo buildUserAdminRolePermission() {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        List<String> roles = new ArrayList<>();
        roles.add("roleA");
        roles.add("roleB");
        roles.add("roleC");
        simpleAuthorizationInfo.addRoles(roles);
        List<String> permissions = new ArrayList<>();
        permissions.add("permissionsA");
        permissions.add("permissionsB");
        permissions.add("permissionsC");
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }
}
