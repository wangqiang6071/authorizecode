package com.zelu.authorizecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.emus.UserEmus;
import com.zelu.authorizecode.entity.AuthortyUser;
import com.zelu.authorizecode.dao.AuthortyUserMapper;
import com.zelu.authorizecode.entity.params.AuthortyParams;
import com.zelu.authorizecode.service.IAuthortyUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.authorizecode.shiro.token.EasyTypeToken;
import com.zelu.authorizecode.utils.JWTUtils;
import com.zelu.authorizecode.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.security.action.GetPropertyAction;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * website后台用户 服务实现类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Slf4j
@Service
public class AuthortyUserServiceImpl extends ServiceImpl<AuthortyUserMapper, AuthortyUser> implements IAuthortyUserService {
    @Value("${shiro.password.salt}")
    private String salt;
    @Value("${shiro.password.random}")
    private int random;
    @Override
    public ServerResponse<Map<String, Object>> userAccountLogin(String account, String password) {
        if(StringUtils.isBlank(account)){
            return ServerResponse.createByError("请输入账号");
        }
        if(StringUtils.isBlank(password)){
            return ServerResponse.createByError("请输入密码");
        }
        EasyTypeToken token=new EasyTypeToken(account,password);
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            throw new UnknownAccountException("账号错误");
        } catch (IncorrectCredentialsException e) {
            throw new IncorrectCredentialsException("密码错误");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (subject.isAuthenticated()) {
            //获取当前系统的名字
            final String s = AccessController.doPrivileged(new GetPropertyAction("os.name"));
            log.info("当前系统的名字:"+s);
            map.put("authToken", subject.getSession().getId());
            map.put("UUID",MD5Utils.getUUID());//系统的uuid
            map.put("hardDisk",MD5Utils.getHardDiskNo());//系统的硬盘id
            return ServerResponse.createBySuccess("登陆成功", map);
        }
        return ServerResponse.createByError("登陆失败", map);
    }

    @Override
    public ServerResponse<String> userAccountLoginOut() {
        try {
            final Subject subject = SecurityUtils.getSubject();
            AuthortyUser user=(AuthortyUser)subject.getPrincipal();
            MD5Utils.getRedisTempate().opsForHash().delete("authenticationCacheName",user.getAccount());
            subject.logout();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return ServerResponse.createBySuccess("账号退出成功");
    }

    @Override
    public ServerResponse<IPage<AuthortyUser>> User_List(AuthortyParams param) {
        Page<AuthortyUser> itempage= new Page<>(param.getIndex(),param.getSize());
        QueryWrapper<AuthortyUser> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(param.getAccount())){
            queryWrapper.like("account",param.getAccount());
        }
        if(StringUtils.isNotBlank(param.getUserName())){
            queryWrapper.like("user_name",param.getUserName());
        }
        if(StringUtils.isNotBlank(param.getPhone())){
            queryWrapper.like("phone",param.getPhone());
        }
        IPage<AuthortyUser> recordIPage = this.baseMapper.selectPage(itempage, queryWrapper);
        recordIPage.getRecords().stream().forEach(a->a.setPassword(null));
        return ServerResponse.createBySuccess(recordIPage);
    }

    @Override
    public ServerResponse<String> updateUser(AuthortyParams param) {
        if(StringUtils.isBlank(param.getUserNo())){
            return ServerResponse.createByError("用户编号不能为为空");
        }
        QueryWrapper<AuthortyUser>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_no",param.getUserNo());
        AuthortyUser managerUser = this.baseMapper.selectOne(queryWrapper);
        if(managerUser==null){
            return ServerResponse.createByError("用户编号不存在");
        }

        if(StringUtils.isNotBlank(param.getPhone())){
            if(! MD5Utils.isChinaPhoneLegal(param.getPhone())){
                return ServerResponse.createByError("传入的手机号不合法");
            }
        }
        if(StringUtils.isBlank(param.getAccount())){
            return ServerResponse.createByError("传入的账号不能为空");
        }
        if(StringUtils.isBlank(param.getPassword())){
            return ServerResponse.createByError("传入的密码不能为空");
        }
        //检查一下用户名字
        if(StringUtils.isBlank(param.getUserName())){
            return ServerResponse.createByError("传入的用户名字不能为空");
        }
        //检验密码和账号的合法性以及密码的加密
        if(!MD5Utils.validateStrEnglish(param.getAccount())){
            return ServerResponse.createByError("账号不合法");
        }
        if(!MD5Utils.validateStrEnglish(param.getPassword())){
            return ServerResponse.createByError("密码不合法");
        }
        //账号状态
        if(StringUtils.isBlank(UserEmus.getEnumType(param.getStatus()))){
            return ServerResponse.createByError("传入的账号状态不正确");
        }
        //账号是否存在
        if(!StringUtils.equals(param.getAccount(),managerUser.getAccount())){
            //查询一下账号是否重复
            QueryWrapper<AuthortyUser>queryWrapper2=new QueryWrapper<>();
            queryWrapper2.eq("account",param.getAccount());
            AuthortyUser managerUser2 = this.baseMapper.selectOne(queryWrapper2);
            if(managerUser2!=null){
                return ServerResponse.createByError("用户账号已存在");
            }
        }
        //进行更新
        managerUser.setUserName(param.getUserName());
        managerUser.setAccount(param.getAccount());
        managerUser.setPassword(JWTUtils.addMd5Hash(param.getPassword(),random,salt));
        managerUser.setPhone(param.getPhone());
        managerUser.setSex(param.getSex());
        managerUser.setStatus(param.getStatus());
        final int count = this.baseMapper.updateById(managerUser);
        if(count==0){
            return ServerResponse.createByError("管理员账号更新失败");
        }
        return ServerResponse.createBySuccess("管理员账号更新成功");
    }

    @Override
    public ServerResponse<String> deleteUser(String user_no) {
        if(StringUtils.isBlank(user_no)){
            return ServerResponse.createByError("用户编号不能为为空");
        }
        QueryWrapper<AuthortyUser>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_no",user_no);
        AuthortyUser managerUser = this.baseMapper.selectOne(queryWrapper);
        if(managerUser==null){
            return ServerResponse.createByError("用户编号不存在");
        }
        final int delete = this.baseMapper.delete(queryWrapper);
        if(delete==0){
            return ServerResponse.createByError("管理员账号删除失败");
        }
        return ServerResponse.createBySuccess("管理员账号删除成功");
    }

    @Override
    public ServerResponse<String> addUser(AuthortyParams param) {
        if(StringUtils.isNotBlank(param.getPhone())){
            if(!MD5Utils.isChinaPhoneLegal(param.getPhone())){
                return ServerResponse.createByError("传入的手机号不合法");
            }
        }
        if(StringUtils.isBlank(param.getAccount())){
            return ServerResponse.createByError("传入的账号不能为空");
        }
        if(StringUtils.isBlank(param.getPassword())){
            return ServerResponse.createByError("传入的密码不能为空");
        }
        //检查一下用户名字
        if(StringUtils.isBlank(param.getUserName())){
            return ServerResponse.createByError("传入的用户名字不能为空");
        }
        //检验密码和账号的合法性以及密码的加密
        if(!MD5Utils.validateStrEnglish(param.getAccount())){
            return ServerResponse.createByError("账号不合法");
        }
        if(!MD5Utils.validateStrEnglish(param.getPassword())){
            return ServerResponse.createByError("密码不合法");
        }
        //账号状态
        if(StringUtils.isBlank(UserEmus.getEnumType(param.getStatus()))){
            return ServerResponse.createByError("传入的账号状态不正确");
        }
        //账号是否存在
        QueryWrapper<AuthortyUser>queryWrapper1=new QueryWrapper<>();
        queryWrapper1.eq("account",param.getAccount());
        AuthortyUser managerUser1 = this.baseMapper.selectOne(queryWrapper1);
        if(managerUser1!=null){
            return ServerResponse.createByError("账号已存在");
        }
        final String stringId = JWTUtils.getStringId();
        AuthortyUser managerUser=new AuthortyUser();
        BeanUtils.copyProperties(param,managerUser);
        managerUser.setUserNo(stringId);
        managerUser.setPassword(JWTUtils.addMd5Hash(param.getPassword(),random,salt));
        int count=this.baseMapper.insert(managerUser);
        if(count==0){
            return ServerResponse.createByError("管理员账号创建失败");
        }
        return ServerResponse.createBySuccess("管理员账号创建成功");
    }
}
