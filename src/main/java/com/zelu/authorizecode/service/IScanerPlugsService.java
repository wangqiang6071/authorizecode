package com.zelu.authorizecode.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerPlugs;
import com.zelu.authorizecode.entity.ScanerPlugsParams;

import java.util.List;

/**
 * <p>
 * 所有插件列表 服务类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
public interface IScanerPlugsService  {
    //增加更新插件
    public ServerResponse<String> add_update_plugs(ScanerPlugs params);
    //增加更新插件参数
    public ServerResponse<String> add_update_plugs_params(ScanerPlugsParams params);
    //删除插件
    public ServerResponse<String> delete_plugs(String PlugsNo);
    //删除插件参数
    public ServerResponse<String> delete_plugs_params(String plugsNo);
    //查询插件参数
    public ServerResponse<ScanerPlugsParams> get_plugs_params_by_plugno(String plugsNo);
    //插件列表
    public ServerResponse<List<ScanerPlugs>> plugs_list();

}
