package com.zelu.authorizecode.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zelu.authorizecode.dao.AuthortyUserMapper;
import com.zelu.authorizecode.emus.UserEmus;
import com.zelu.authorizecode.entity.AuthortyUser;
import com.zelu.authorizecode.shiro.token.EasyTypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

//shiro身份校验核心类
@Slf4j
public class MyRealm extends AuthorizingRealm {

	@Value("${shiro.password.time}")
	private Integer time;
	@Value("${shiro.password.salt}")
	private String salt;
	@Autowired
	private AuthortyUserMapper userMapper;

	public String getName() {
		return "MyRealm";
	}
	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
//		EmailUser account=(EmailUser)principals.getPrimaryPrincipal();
//		List<EmailRole> user= userService.selectRoleListByUserAccount(account.getAccount());
//		System.err.println("user===>"+user);
//		if(user==null){
//			throw new StringException("当前用户授权错误");
//		}
//		for(EmailRole roleList:user){
//			info.addRole(roleList.getRoleName());
//			//角色下有多少个权限点
//			List<EmailPermission> emailPermissions = roleListMapper.selectPermissonByRoleId(roleList.getId());
//			System.err.println("emailPermissions===>"+emailPermissions);
//			for(EmailPermission emailPermission:emailPermissions){
//				info.addStringPermission(emailPermission.getPermissionKey());
//			}
//		}
		return null;
	}

	// 用户信息认证.(身份验证) 是用来验证用户身份
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 用户名：Principal 密码：Credentials
		EasyTypeToken EasyToken = (EasyTypeToken) token;
		String OnlyKey=EasyToken.getUsername();
		//登陆的账号是唯一的
		QueryWrapper<AuthortyUser>queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("account",OnlyKey);
		AuthortyUser users=userMapper.selectOne(queryWrapper);
		if(users.getStatus().equals(UserEmus.forbidden.getMsg())){
			throw new DisabledAccountException("帐号已被禁止登录");
		}
		//身份认证成功：登录成功
		// 参数1：数据库中的对象，参数2:数据库的密码，参数3:密码加密的加盐值，参数4:MyRealm名字
		return new SimpleAuthenticationInfo(users, users.getPassword(),new ByteSourceUtils(salt), getName());
	}
}