package com.zelu.authorizecode.service;

import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.AuthortyModel;
import com.zelu.authorizecode.entity.AuthortyRole;
import com.zelu.authorizecode.entity.AuthortyUserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.authorizecode.entity.params.AuthortyRoleModelParams;

import java.util.List;

/**
 * <p>
 * 用户角色关系  服务类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
public interface IAuthortyUserRoleService extends IService<AuthortyUserRole> {
    //用户与角色添加
    public ServerResponse<String> AddUserRole(String userNo,String roleNo);

    //当前用户下有哪些角色
    public ServerResponse<AuthortyRole> SelectByUserRole(String userNo);

    //当前用户下有哪些模块
    public ServerResponse<List<AuthortyModel>> SelectByUserModel(String userNo);



}
