package com.zelu.authorizecode.service;

import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.SancerFinalCleanData;
import com.zelu.authorizecode.entity.ScanerGropPlugsData;
import com.zelu.authorizecode.entity.params.SanerPlugsRedisDataParams;
import com.zelu.authorizecode.utils.Rquest.entity.RequestModel;
import com.zelu.authorizecode.utils.Rquest.entity.RedisParams;
import com.zelu.authorizecode.utils.Rquest.entity.plugsList.ReturnPlugsListResult;
import com.zelu.authorizecode.utils.Rquest.entity.plugsdate.ReturnPlugsDate;
import com.zelu.authorizecode.utils.Rquest.entity.redisdata.ResultFinalFuctionData;

import java.util.List;

/**
 * <p>
 * 任务中的插件扫描数据 服务类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
public interface IScanerReportDataService {
    //获取所有的插件列表数据
    public ServerResponse<ReturnPlugsListResult> plugs_list_data(RequestModel model);

    //获取插件中task_id数据
    public ServerResponse<ReturnPlugsDate> plugs_data(RequestModel model);

    //获取插件的原生数据
    public ServerResponse<SanerPlugsRedisDataParams>redis_date(RedisParams result);

    //============================================================================


    //获取base插件的清洗后的数据
    public ServerResponse<SancerFinalCleanData>redis_clean_date(RedisParams result);

    //获取fuction插件的数据
    public ServerResponse<SanerPlugsRedisDataParams> redis_fuction_data(RedisParams params);

}
