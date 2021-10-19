package com.zelu.authorizecode.entity.params;

import lombok.Data;
import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/9/3 14:23
 */
@Data
public class AuthortyRoleModelParams {
    /**
     * 角色编号
     */
    private String roleNo;

    private String roleName;
    /**
     * 模块编号list
     */
    private List<String> modelList;
}
