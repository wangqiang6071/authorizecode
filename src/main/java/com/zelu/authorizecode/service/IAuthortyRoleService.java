package com.zelu.authorizecode.service;

import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.AuthortyRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.authorizecode.entity.params.AuthortyRoleModelParams;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * <p>
 * 角色列表  服务类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-03
 */
public interface IAuthortyRoleService extends IService<AuthortyRole> {

    //添加角色：角色下的模块列表
    public ServerResponse<String> AddRole(AuthortyRoleModelParams params);

    //更新角色：角色下的模块列表
    public ServerResponse<String> UpdateRole(AuthortyRoleModelParams params);

    //删除角色：角色下的模块列表
    public ServerResponse<String> DeleteRole(String roleNo);

    //角色列表：角色下的模块列表
    public ServerResponse<List<AuthortyRole>> RoleList();

}
