package com.zelu.authorizecode.entity.params;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/9/3 16:23
 */
@Data
public class AuthortyCodeParams {
    /**
     * 系统uid
     */
    private String systemUuid;

    /**
     * 系统硬盘编号
     */
    private String systemDiskno;

    /**
     * 授权密钥
     */
    private String authortyKey;

    /**
     * 密钥进行MD5加密
     * */
    private int random;

    /*
     * 密钥进行MD5加密的盐值
     */
    private String salt;

    /**
     * 服务器uuid进行MD5加密
     * */
    private int random1;

    /*
     * 服务器uuid进行MD5加密的盐值
     */
    private String salt1;

    /**
     * 硬盘id进行MD5加密
     * */
    private int random2;

    /*
     * 硬盘id进行MD5加密的盐值
     */
    private String salt2;
    /**
     * 授权截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime periodofValidity;

    /**
     * 授权公司
     */
    private String companyName;

    /**授权的模块*/
    private List<String>modelNo;
}
