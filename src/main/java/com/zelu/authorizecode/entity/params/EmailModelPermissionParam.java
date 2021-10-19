package com.zelu.authorizecode.entity.params;

import com.zelu.authorizecode.entity.AuthortyModelPermisstion;
import lombok.Data;

import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/10/14 17:46
 */
@Data
public class EmailModelPermissionParam {
        private Integer id;
        private String modelId;
        private String permissionName;

        private String roleName;

        List<AuthortyModelPermisstion> permissions;


}
