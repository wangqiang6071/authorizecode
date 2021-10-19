package com.zelu.authorizecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.dao.AuthortyModelMapper;
import com.zelu.authorizecode.dao.AuthortyRoleModelMapper;
import com.zelu.authorizecode.entity.AuthortyModel;
import com.zelu.authorizecode.entity.AuthortyRole;
import com.zelu.authorizecode.dao.AuthortyRoleMapper;
import com.zelu.authorizecode.entity.AuthortyRoleModel;
import com.zelu.authorizecode.entity.params.AuthortyRoleModelParams;
import com.zelu.authorizecode.service.IAuthortyRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.authorizecode.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色列表  服务实现类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@Service
public class AuthortyRoleServiceImpl extends ServiceImpl<AuthortyRoleMapper, AuthortyRole> implements IAuthortyRoleService {

    @Autowired
    private AuthortyModelMapper modelMapper;
    @Autowired
    private AuthortyRoleModelMapper roleModelMapper;
    @Override
    public ServerResponse<String> AddRole(AuthortyRoleModelParams params) {
        if(StringUtils.isBlank(params.getRoleName())){
            return ServerResponse.createByError("角色的名字不能为空");
        }
        //查询一下角色的名字是否存在
        QueryWrapper<AuthortyRole>roleQueryWrapper=new QueryWrapper<>();
        roleQueryWrapper.eq("role_name",params.getRoleName());
        if(this.baseMapper.selectCount(roleQueryWrapper)>0){
            return ServerResponse.createByError("角色名字已经存在");
        }
        //角色对象
        final String roleno=JWTUtils.getStringId();
        AuthortyRole role=new AuthortyRole();
        role.setRoleNo(roleno);
        role.setRoleName(params.getRoleName());
        //校验模块对象
        for(String modelno:params.getModelList()){
            QueryWrapper<AuthortyModel>modelQueryWrapper=new QueryWrapper<>();
            modelQueryWrapper.eq("model_no",modelno);
            final AuthortyModel model = modelMapper.selectOne(modelQueryWrapper);
            if(model==null){
                return ServerResponse.createByError("当前角色添加的模块不存在");
            }
            AuthortyRoleModel roleModel=new AuthortyRoleModel();
            roleModel.setRoleNo(roleno);
            roleModel.setModelNo(modelno);
            roleModelMapper.insert(roleModel);
        }
        final int insert = this.baseMapper.insert(role);
        if(insert==0){
            return ServerResponse.createByError("角色添加失败");
        }
        return ServerResponse.createBySuccess("角色添加成功");
    }

    @Override
    public ServerResponse<String> UpdateRole(AuthortyRoleModelParams params) {
        if(StringUtils.isBlank(params.getRoleNo())){
            return ServerResponse.createByError("角色编号不能为空");
        }
        if(StringUtils.isBlank(params.getRoleName())){
            return ServerResponse.createByError("角色名字不能为空");
        }
        //删除role_model之前的数据
        Map<String,Object> map=new HashMap<>();
        map.put("role_no",params.getRoleNo());
        roleModelMapper.deleteByMap(map);
        //删除role的数据
        this.baseMapper.deleteByMap(map);
        this.AddRole(params);
        return ServerResponse.createBySuccess("角色更新成功");
    }

    @Override
    public ServerResponse<String> DeleteRole(String roleNo) {
        if(StringUtils.isBlank(roleNo)){
            return ServerResponse.createByError("角色编号不能为空");
        }
        //删除role_model之前的数据
        Map<String,Object> map=new HashMap<>();
        map.put("role_no",roleNo);
        roleModelMapper.deleteByMap(map);
        //删除role的数据
        final int i = this.baseMapper.deleteByMap(map);
        if(i==0){
            return ServerResponse.createByError("角色删除失败");
        }
        return ServerResponse.createBySuccess("角色删除成功");
    }

    @Override
    public ServerResponse<List<AuthortyRole>> RoleList() {
        final List<AuthortyRole> authortyRoles = this.baseMapper.selectList(new QueryWrapper<>());
        return ServerResponse.createBySuccess(authortyRoles);
    }
}
