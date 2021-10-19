package com.zelu.authorizecode.entity.params;

import com.zelu.authorizecode.utils.Rquest.entity.RequestModel;
import lombok.Data;

import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/9/23 19:10
 */
@Data
public class SanerPlugsParams {
    /**
     * 插件编号
     */
    private String plugsNo;

    /**
     * 插件的类型:基础数据(base) 功能性(fuction)
     */
    private String type;

    /**
     * 如果是功能性 wife bluetooth
     */
    private String typeName;

    /**
     * 插件所需要的参数(允许多个)
     */
    private List<RequestModel> requestModels;



    //以下查询插件使用
    /**
     * 插件的唯一标识id
     */
    private String plugsId;

    /**
     * 插件名字
     */
    private String plugsName;
}
