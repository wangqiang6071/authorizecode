package com.zelu.authorizecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.dao.*;
import com.zelu.authorizecode.entity.*;
import com.zelu.authorizecode.service.IAuthortyUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户角色关系  服务实现类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Service
public class AuthortyUserRoleServiceImpl extends ServiceImpl<AuthortyUserRoleMapper, AuthortyUserRole> implements IAuthortyUserRoleService {

    @Autowired
    private AuthortyUserMapper userMapper;
    @Autowired
    private AuthortyRoleMapper roleMapper;
    @Autowired
    private AuthortyRoleModelMapper roleModelMapper;
    @Autowired
    private AuthortyModelMapper modelMapper;
    @Override
    public ServerResponse<String> AddUserRole(String userNo, String roleNo) {
        //检查一下用户和角色的编号是否正确
        if(StringUtils.isBlank(userNo)){
            return ServerResponse.createByError("用户编号不能为空");
        }
        if(StringUtils.isBlank(roleNo)){
            return ServerResponse.createByError("角色编号不能为空");
        }
        QueryWrapper<AuthortyUser>userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.eq("user_no",userNo);
        final AuthortyUser authortyUser = userMapper.selectOne(userQueryWrapper);
        if(authortyUser==null){
            return ServerResponse.createByError("传入的用户编号错误");
        }
        QueryWrapper<AuthortyRole>roleQueryWrapper=new QueryWrapper<>();
        roleQueryWrapper.eq("role_no",roleNo);
        final AuthortyRole role = roleMapper.selectOne(roleQueryWrapper);
        if(role==null){
            return ServerResponse.createByError("传入的角色编号错误");
        }
        QueryWrapper<AuthortyUserRole>userroleQueryWrapper=new QueryWrapper<>();
        userroleQueryWrapper.eq("user_no",userNo);
        this.baseMapper.delete(userroleQueryWrapper);
        AuthortyUserRole userRole=new AuthortyUserRole();
        userRole.setUserNo(userNo);
        userRole.setRoleNo(roleNo);
        final int insert = this.baseMapper.insert(userRole);
        if(insert==0){
            return ServerResponse.createByError("用户与角色添加失败");
        }
        return ServerResponse.createBySuccess("用户与角色添加成功");
    }

    @Override
    public ServerResponse<AuthortyRole> SelectByUserRole(String userNo) {
        //查询哪个用户
        QueryWrapper<AuthortyUserRole>userroleQueryWrapper=new QueryWrapper<>();
        userroleQueryWrapper.eq("user_no",userNo);
        final AuthortyUserRole userRole = this.baseMapper.selectOne(userroleQueryWrapper);
        QueryWrapper<AuthortyRole>userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.eq("role_no",userRole.getRoleNo());
        final AuthortyRole role = roleMapper.selectOne(userQueryWrapper);
        return ServerResponse.createBySuccess(role);
    }

    @Override
    public ServerResponse<List<AuthortyModel>> SelectByUserModel(String userNo) {
        final ServerResponse<AuthortyRole> authortyRoleServerResponse = this.SelectByUserRole(userNo);
        final AuthortyRole data = authortyRoleServerResponse.getData();
        List<AuthortyModel>models=new ArrayList<>();
        QueryWrapper<AuthortyRoleModel>roleModel=new QueryWrapper<>();
        roleModel.eq("role_no",data.getRoleNo());
        final List<AuthortyRoleModel> authortyRoleModels = roleModelMapper.selectList(roleModel);
        for(AuthortyRoleModel authortyRoleModel:authortyRoleModels){
            QueryWrapper<AuthortyModel>Model=new QueryWrapper<>();
            Model.eq("model_no",authortyRoleModel.getModelNo());
            final AuthortyModel model = modelMapper.selectOne(Model);
            models.add(model);
        }
        return ServerResponse.createBySuccess(models);
    }
}
