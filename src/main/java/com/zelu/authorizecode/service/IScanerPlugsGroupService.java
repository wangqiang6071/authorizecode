package com.zelu.authorizecode.service;

import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerPlugs;
import com.zelu.authorizecode.entity.ScanerPlugsGroup;
import com.zelu.authorizecode.entity.params.SanerPlugsParams;

import java.util.List;

/**
 * <p>
 * 插件与插件组的关系 服务类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
public interface IScanerPlugsGroupService {
    //新建插件组
    public ServerResponse<String>add_update_plug_group(ScanerPlugsGroup params);
    //删除插件组
    public ServerResponse<String>delete_plug_group(String groupNo);
    //根据插件组编号查询对应的插件列表
    public ServerResponse<List<SanerPlugsParams>> get_plug_group_details(String groupNo);
}
