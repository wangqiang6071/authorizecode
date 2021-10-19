package com.zelu.authorizecode.entity.params;

import lombok.Data;

/**
 * @author wangqiang
 * @Date: 2021/9/6 17:28
 */
@Data
public class AuthortyActivationParams {

    private String authortyCode;
    private String sercrtKey;
    private Integer type;//0代表服务器已激活 -1代表服务器未被激活 -2代表服务器已激活但已过期
}
