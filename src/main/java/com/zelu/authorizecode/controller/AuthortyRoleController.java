package com.zelu.authorizecode.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.dao.AuthortyModelMapper;
import com.zelu.authorizecode.dao.AuthortyModelPermissionMapper;
import com.zelu.authorizecode.dao.AuthortyRoleMapper;
import com.zelu.authorizecode.dao.AuthortyRoleModelMapper;
import com.zelu.authorizecode.entity.AuthortyModel;
import com.zelu.authorizecode.entity.AuthortyModelPermisstion;
import com.zelu.authorizecode.entity.AuthortyRole;
import com.zelu.authorizecode.entity.AuthortyRoleModel;
import com.zelu.authorizecode.entity.params.AuthortyRoleModelParams;
import com.zelu.authorizecode.entity.params.EmailModelPermissionParam;
import com.zelu.authorizecode.service.IAuthortyRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 角色列表  前端控制器
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
@RestController
@RequestMapping("/role")
public class AuthortyRoleController {

    @Autowired
    private IAuthortyRoleService roleService;

    @Autowired
    private AuthortyModelMapper modelMapper;

    @Autowired
    private AuthortyModelPermissionMapper modelPermissionMapper;

    @Autowired
    private AuthortyRoleMapper roleMapper;

    @Autowired
    private AuthortyRoleModelMapper roleModelMapper;

    //添加角色：角色下的模块列表
    @PostMapping(value = "add_role")
    public ServerResponse<String> AddRole(@RequestBody AuthortyRoleModelParams params){
        return roleService.AddRole(params);
    }

    //更新角色：角色下的模块列表
    @PostMapping(value = "update_role")
    public ServerResponse<String> UpdateRole(@RequestBody AuthortyRoleModelParams params){
        return roleService.UpdateRole(params);
    }
    //删除角色：角色下的模块列表
    @PostMapping(value = "delete_role")
    public ServerResponse<String> DeleteRole(String roleNo){
        return roleService.DeleteRole(roleNo);
    }
    //角色列表：角色下的模块列表
    @PostMapping(value = "role_list")
    public ServerResponse<List<AuthortyRole>> RoleList(){
        return roleService.RoleList();
    }

    //根据角色查询当前的权限点
    @GetMapping("model_by_role_permissions")
    public ServerResponse<List<EmailModelPermissionParam>>Model_By_Role_Permissions(@RequestParam("role_no") String roleNo){
        if(StringUtils.isBlank(roleNo)){
            return ServerResponse.createByError("角色编号不能为空");
        }
        //当前角色下有哪些模块
        QueryWrapper<AuthortyRole> roleQueryWrapper=new QueryWrapper<>();
        roleQueryWrapper.eq("role_no",roleNo);
        final AuthortyRole role = roleMapper.selectOne(roleQueryWrapper);
        if(role==null){
            return ServerResponse.createByError("传入的角色编号错误");
        }
        //根据角色查询模块
        Map<String,Object> map1=new HashMap<>();
        map1.put("role_no",roleNo);
        final List<AuthortyRoleModel> authortyRoleModels = roleModelMapper.selectByMap(map1);
        final List<AuthortyModel> emailSysModels = new ArrayList<>();
        for(AuthortyRoleModel authortyRoleModel:authortyRoleModels){
            QueryWrapper<AuthortyModel>queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("model_no",authortyRoleModel.getModelNo());
            emailSysModels.add(modelMapper.selectOne(queryWrapper));
        }
        List<EmailModelPermissionParam> listParams=new ArrayList<>();
        for(AuthortyModel emailSysModel:emailSysModels){
            Map<String,Object>map2=new HashMap<>();
            map2.put("model_id",emailSysModel.getId());
            final List<AuthortyModelPermisstion> emailPermissions = modelPermissionMapper.selectByMap(map2);
            EmailModelPermissionParam param=new EmailModelPermissionParam();
            param.setId(emailSysModel.getId());
            param.setModelId(emailSysModel.getModelNo());
            param.setPermissionName(emailSysModel.getModelName());
            param.setPermissions(emailPermissions);
            listParams.add(param);
        }
        return ServerResponse.createBySuccess(listParams);
    }


    //查询所有模块 权限点
    @GetMapping("model_permissions")
    public ServerResponse<List<EmailModelPermissionParam>>Model_Permissions(){
//        Map<String,Object> map=new HashMap<>();
//        List<AuthortyModelPermisstion> emailPermissions = modelPermissionMapper.selectByMap(map);
//        final List<AuthortyModel> emailSysModels = modelMapper.selectByMap(map);
//        List<Map<Integer,List<AuthortyModelPermisstion>>> listMap=new ArrayList<>();
//        for(AuthortyModel emailSysModel:emailSysModels){
//            List<AuthortyModelPermisstion>permissions=new ArrayList<>();
//            Map<Integer,List<AuthortyModelPermisstion>>maps=new HashMap<>();
//            for(AuthortyModelPermisstion emailPermission:emailPermissions){
//                if(emailPermission.getModelId()==emailSysModel.getId()){
//                    permissions.add(emailPermission);
//                }
//            }
//            maps.put(emailSysModel.getId(),permissions);
//            listMap.add(maps);
//        }
//        return ServerResponse.createBySuccess(listMap);
        List<EmailModelPermissionParam> listParams=new ArrayList<>();
        Map<String,Object>map=new HashMap<>();
        List<AuthortyModel> emailSysModels = modelMapper.selectByMap(map);
        for(AuthortyModel emailSysModel:emailSysModels){
            Map<String,Object>map2=new HashMap<>();
            map2.put("model_id",emailSysModel.getId());
            final List<AuthortyModelPermisstion> emailPermissions = modelPermissionMapper.selectByMap(map2);
            EmailModelPermissionParam param=new EmailModelPermissionParam();
            param.setId(emailSysModel.getId());
            param.setModelId(emailSysModel.getModelNo());
            param.setPermissionName(emailSysModel.getModelName());
            param.setPermissions(emailPermissions);
            listParams.add(param);
        }
        return ServerResponse.createBySuccess(listParams);
    }

}
