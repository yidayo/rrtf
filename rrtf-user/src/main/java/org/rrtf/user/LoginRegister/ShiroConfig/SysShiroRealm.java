package org.rrtf.user.LoginRegister.ShiroConfig;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.rrtf.user.LoginRegister.service.UserService;
import org.rrtf.user.entity.Permission;
import org.rrtf.user.entity.Role;
import org.rrtf.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

public class SysShiroRealm extends AuthorizingRealm {

	@Autowired
	UserService userInfoService;

	// 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		System.out.println("返回权限信息的方法");
		// 当前登录系统用户对象
		User user = (User) principals.getPrimaryPrincipal();
		// 依次将用户角色信息添加到AuthorizationInfo
		for (Role r : user.getRoles()) {
			info.addRole(r.getRoleName());
			System.out.println(r.getRoleName());
			for (Permission p : r.getPermissions()) {
				info.addStringPermission(p.getPermission());
				System.out.println(p.getPermission());
			}
		}
		return info;
	}

	// 认证回调函数,登录时调用.
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 获取当前登录的用户名
		String username = (String) token.getPrincipal();
		User user = userInfoService.findService(username);
		if (user == null) {
			return null;
		}
		ByteSource salt = ByteSource.Util.bytes(user.getCredentialsSalt());
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
		System.out.println("info"+info);
		return info;
	}
}
