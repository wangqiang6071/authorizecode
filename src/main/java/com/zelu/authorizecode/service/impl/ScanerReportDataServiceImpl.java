package com.zelu.authorizecode.service.impl;

import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.controller.ScanerReportDataController;
import com.zelu.authorizecode.emus.PlugsEmus;
import com.zelu.authorizecode.entity.*;
import com.zelu.authorizecode.entity.params.PlugsFinalParams;
import com.zelu.authorizecode.entity.params.SanerPlugsRedisDataParams;
import com.zelu.authorizecode.entity.params.ScanerPlugsScanerListParams;
import com.zelu.authorizecode.service.IScanerReportDataService;
import com.zelu.authorizecode.utils.JWTUtils;
import com.zelu.authorizecode.utils.MD5Utils;
import com.zelu.authorizecode.utils.Rquest.RestTemplateUtils;
import com.zelu.authorizecode.utils.Rquest.StartEndUtils;
import com.zelu.authorizecode.utils.Rquest.entity.RequestModel;
import com.zelu.authorizecode.utils.Rquest.entity.RedisParams;
import com.zelu.authorizecode.utils.Rquest.entity.plugsList.ReturnPlugsListResult;
import com.zelu.authorizecode.utils.Rquest.entity.plugsdate.ReturnPlugsDate;
import com.zelu.authorizecode.utils.Rquest.entity.redisdata.Out1;
import com.zelu.authorizecode.utils.Rquest.entity.redisdata.Out2;
import com.zelu.authorizecode.utils.Rquest.entity.redisdata.ResultFinalNotCleanData;
import com.zelu.authorizecode.utils.Rquest.entity.redisdata.ResultFinalFuctionData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务中的插件扫描数据 服务实现类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
@Service
@Slf4j
public class ScanerReportDataServiceImpl implements IScanerReportDataService {
    @Autowired
    private MongoTemplate repository;
    @Autowired
    @Qualifier("urlConnection")
    private RestTemplate restTemplate;

    @Value("${intface.address}")
    private String url;

    //获取所有的插件列表数据
    @Override
    public ServerResponse<ReturnPlugsListResult> plugs_list_data(RequestModel model) {
        String pluginType = (String) model.getParams().get(0);
        Map<String, Object> map = (Map<String, Object>) model.getParams().get(1);
        RestTemplateUtils<ReturnPlugsListResult> restTemplateUtils = new RestTemplateUtils<>(ReturnPlugsListResult.class, restTemplate);
        ReturnPlugsListResult result = restTemplateUtils.RestTemplate(url, pluginType, map);
        return ServerResponse.createBySuccess(result);
    }

    //获取插件数据 task_id
    @Override
    public ServerResponse<ReturnPlugsDate> plugs_data(RequestModel model) {
        String pluginType = (String) model.getParams().get(0);
        Map<String, Object> map = (Map<String, Object>) model.getParams().get(1);
        String id = (String) map.get("id");
        if (StringUtils.isNotBlank(id)) {
            Criteria criteria = new Criteria();
            criteria.andOperator(criteria.where("plugs_id").is(id));
            Query query = new Query(criteria);
            final ScanerPlugs one = repository.findOne(query, ScanerPlugs.class);
            if (one == null) {
                return ServerResponse.createByError("插件的id:" + id + ",错误");
            }
        }
        RestTemplateUtils<ReturnPlugsDate> restTemplateUtils = new RestTemplateUtils<>(ReturnPlugsDate.class, restTemplate);
        ReturnPlugsDate result = restTemplateUtils.RestTemplate(url, pluginType, map);
        return ServerResponse.createBySuccess(result);
    }

    //获取原生数据result
    @Override
    public ServerResponse<SanerPlugsRedisDataParams> redis_date(RedisParams result) {
        Map<String, Object> map = new HashMap<>();
        map.put("task_id", result.getTaskId());
        RestTemplateUtils<SanerPlugsRedisDataParams> restTemplateUtils = new RestTemplateUtils<>(SanerPlugsRedisDataParams.class, restTemplate);
        SanerPlugsRedisDataParams results = restTemplateUtils.RestTemplate(url, "get_task_result", map);
        return ServerResponse.createBySuccess(results);
    }

    //============================================================================

    @Autowired
    private StartEndUtils startEndUtils;
    @Autowired
    private ScanerReportDataController fuctioncontroller;

    //fuction读取数据
    @Override
    public ServerResponse<SanerPlugsRedisDataParams> redis_fuction_data(RedisParams params) {
        //查询一下任务是否存在
        final ScanerTask task = repository.findById(params.getTaskNo(), ScanerTask.class);
        if (task == null) {
            return ServerResponse.createByError("传入的任务编号不存在");
        }
        //wife或者蓝牙的唯一值 uuid
        if (StringUtils.isBlank(params.getOnlyKey())) {
            return ServerResponse.createByError("function类别的插件唯一值不能为空");
        }
        //搜索的关键词
        if (StringUtils.isBlank(params.getTargetName())) {
            return ServerResponse.createByError("function类别的插件目标值不能为空");
        }
        //查询一下插件是否存在
        final ScanerPlugs Plugs = repository.findById(params.getPlugsNo(), ScanerPlugs.class);
        if (Plugs == null) {
            return ServerResponse.createByError("传入的插件编号不存在");
        }
        //task_id
        final String task_id = this.plugs_data(params.getModel()).getData().getTask_id();
        if (StringUtils.isBlank(params.getPlugsNo())) {
            return ServerResponse.createByError("插件id不能为空");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("task_id", task_id);
        RestTemplateUtils<SanerPlugsRedisDataParams> restTemplateUtils = new RestTemplateUtils<>(SanerPlugsRedisDataParams.class, restTemplate);
        SanerPlugsRedisDataParams result = restTemplateUtils.RestTemplate(url, "get_task_result", map);
        while (result.getOut().getStatus()==null || result.getOut().getStatus().equals("start") || result.getOut().getStatus().equals("working")) {
            log.info("进入wile循环3==》" + task_id);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = restTemplateUtils.RestTemplate(url, "get_task_result", map);
            if (result.getOut().getStatus().equals("end")) {

                if (StringUtils.equals(Plugs.getType(), PlugsEmus.Base.getMsg())) {
                    ScanerPlugsScanerListParams params1 = new ScanerPlugsScanerListParams();
                    params1.setTaskNo(params.getTaskNo());
                    List<String> plugsList = new ArrayList<>();
                    plugsList.add(params.getPlugsNo());
                    params1.setPlugsNo(plugsList);
                    fuctioncontroller.plugs_base_scaners(params1);
                } else if (StringUtils.equals(Plugs.getType(), PlugsEmus.Fuction.getMsg())) {
                    //查询一下是否有重复的数据同一个uuid
                    ResultFinalFuctionData fuctionData = repository.findById(params.getOnlyKey(), ResultFinalFuctionData.class);
                    if (fuctionData != null) {
                        //查询一下 扫描目标 去除重复
                        List<SanerPlugsRedisDataParams> collects = fuctionData.getParams().stream().filter(list -> !StringUtils.equals(list.getTargetName(), params.getTargetName())).collect(Collectors.toList());
                        result.setGroupNo(params.getGroupNo());
                        result.setPlugsNo(params.getPlugsNo());
                        result.setTypeName(Plugs.getTypeName());
                        collects.add(result);
                        result.setTargetName(params.getTargetName());
                        fuctionData.setTargetNo(params.getOnlyKey());
                        fuctionData.setParams(collects);
                        fuctionData.setCreateTime(LocalDateTime.now());
                        //更新数据库数据
                        final ResultFinalFuctionData save = repository.save(fuctionData);
                    } else {
                        ResultFinalFuctionData newfuctionData = new ResultFinalFuctionData();
                        List<SanerPlugsRedisDataParams> results = new ArrayList();
                        result.setTargetName(params.getTargetName());
                        result.setGroupNo(params.getGroupNo());
                        result.setPlugsNo(params.getPlugsNo());
                        newfuctionData.setTargetNo(params.getOnlyKey());
                        result.setTypeName(Plugs.getTypeName());
                        result.setTaskNo(params.getTaskNo());
                        results.add(result);
                        newfuctionData.setParams(results);
                        newfuctionData.setCreateTime(LocalDateTime.now());
                        //新增数据库数据
                        final ResultFinalFuctionData save = repository.save(newfuctionData);
                    }
                }
                return ServerResponse.createBySuccess(result);
            }
        }
        return null;
    }

    //base插件读取redis的最终数据==多个base插件的数据
    @Override
    public ServerResponse<SancerFinalCleanData> redis_clean_date(RedisParams params) {
        if(StringUtils.isBlank(params.getTaskId())){
            return ServerResponse.createByError("任务id不能为空");
        }
        if(StringUtils.isBlank(params.getPlugsNo())){
            return ServerResponse.createByError("插件id不能为空");
        }
        //查询一下任务是否存在
        final ScanerTask task = repository.findById(params.getTaskNo(), ScanerTask.class);
        if(task==null){
            return ServerResponse.createByError("传入的任务编号不存在");
        }
        //查询一下插件是否存在
        final ScanerPlugs Plugs = repository.findById(params.getPlugsNo(), ScanerPlugs.class);
        if(Plugs==null){
            return ServerResponse.createByError("传入的插件编号不存在");
        }
        Map<String,Object>map=new HashMap<>();
        map.put("task_id",params.getTaskId());
        RestTemplateUtils<SanerPlugsRedisDataParams> restTemplateUtils=new RestTemplateUtils<>(SanerPlugsRedisDataParams.class,restTemplate);
        //等待一段时间
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SanerPlugsRedisDataParams result=restTemplateUtils.RestTemplate(url, "get_task_result", map);
        //返回给前端的数据=清洗后的数据
        List<PlugsFinalParams> finalParams=new ArrayList<>();
        SancerFinalCleanData finalData=new SancerFinalCleanData();
        finalData.setTaskNo(params.getTaskNo());
        //进行保存数据 哪个任务下的哪个任务组 任务组中的哪个插件的数据
            if(StringUtils.equals(Plugs.getType(), PlugsEmus.Base.getMsg())) {
                //查询一下当前插件组中是否存在之前的数据==原始数据
                ResultFinalNotCleanData FinalData = repository.findById(params.getTaskNo(), ResultFinalNotCleanData.class);
                if (FinalData == null) {
                    ResultFinalNotCleanData newFinalData = new ResultFinalNotCleanData();
                    newFinalData.setTaskNo(params.getTaskNo());

                    List<SanerPlugsRedisDataParams> collects = new ArrayList<>();
                    //插件组编号
                    result.setGroupNo(params.getGroupNo());
                    //插件编号
                    result.setPlugsNo(params.getPlugsNo());
                    //插件的类型名字
                    result.setTypeName(Plugs.getTypeName());
                    //判断插件的数据类型
                    if (StringUtils.equals(Plugs.getTypeName(), PlugsEmus.Wifi.getMsg())) {
                        //解决wife-Name名字
                        if (result.getOut().getOut().getNetworks() != null) {
                            //清洗wife的数据
                            List<Map<String, String>> mapLists = result.getOut().getOut().getNetworks();
                            for (Map<String, String> maps : mapLists) {
                                PlugsFinalParams paramss = new PlugsFinalParams();
                                for (Map.Entry<String, String> entry : maps.entrySet()) {
                                    String str = MD5Utils.decodeUTF8Str(entry.getValue());
                                    entry.setValue(str);
                                    if (StringUtils.equals(entry.getKey(), "Mode")) {
                                        entry.setValue(JWTUtils.getStringId());
                                    }
                                }
                                paramss.setOnlyKey(JWTUtils.getStringId());
                                paramss.setGroupNo(params.getGroupNo());
                                paramss.setPlugsNo(params.getPlugsNo());
                                paramss.setTargetName(maps.get("Name"));
                                paramss.setPlugsMac(maps.get("Address"));
                                paramss.setPlugsName(Plugs.getPlugsName());
                                paramss.setPlugsType(PlugsEmus.Wifi.getMsg());
                                paramss.setTime(LocalDateTime.now());
                                finalParams.add(paramss);
                            }
                            log.info(String.format("数据库为空时wifi扫描的数据===》%s",result.getOut().getOut().getNetworks()));
                            collects.add(result);
                            newFinalData.setRedisDataList(collects);
                            //新增数据库数据
                            final ResultFinalNotCleanData save = repository.save(newFinalData);
                        }
                    } else if (StringUtils.equals(Plugs.getTypeName(), PlugsEmus.Bluetooth.getMsg())) {
                        //清洗wife的数据
                        final Out2 out = result.getOut().getOut();
                        if (out != null) {
                            final List<Object> devices = out.getDevices();
                            for (int i = 0; i < devices.size(); i++) {
                                PlugsFinalParams paramss = new PlugsFinalParams();
                                List<Object> listObj = (List<Object>) devices.get(i);
                                log.info("数据库为为空的情况，发现蓝牙的数量==》"+devices.size());
                                final String BulothMac = (String) listObj.get(0);
                                final String BulothName = (String) listObj.get(1);
                                //final Long BulothNum = (Long)listObj.get(2);
                                paramss.setOnlyKey(JWTUtils.getStringId());
                                paramss.setGroupNo(params.getGroupNo());
                                paramss.setPlugsNo(params.getPlugsNo());
                                paramss.setTargetName(BulothName);
                                paramss.setPlugsMac(BulothMac);
                                paramss.setPlugsName(Plugs.getPlugsName());
                                if(StringUtils.equals(PlugsEmus.Bluetooth.getMsg(),Plugs.getPlugsId().split("/")[1])){
                                    paramss.setPlugsType(PlugsEmus.Bluetooth_Br.getMsg());//br
                                }
                                paramss.setPlugsType(Plugs.getPlugsId().split("/")[1]);//ble
                                paramss.setTime(LocalDateTime.now());
                                finalParams.add(paramss);
                            }
                            log.info(String.format("数据库为空时Bluetooth扫描的数据===》%s",result.getOut().getOut().getDevices()));
                        } else {
                            PlugsFinalParams paramss = new PlugsFinalParams();
                            // Out1(out=null, status=end, error=module load fail, time=1634262567)
                            final Out1 out1 = result.getOut();
                            paramss.setOnlyKey(JWTUtils.getStringId());
                            paramss.setGroupNo(params.getGroupNo());
                            paramss.setPlugsNo(params.getPlugsNo());
                            paramss.setTargetName(out1.getError());
                            paramss.setPlugsName(Plugs.getPlugsName());
                            paramss.setPlugsType(Plugs.getPlugsId().split("/")[1]);
                            paramss.setTime(LocalDateTime.now());
                            finalParams.add(paramss);
                        }
                        //查询一下之前是否存在相同的数据
                        collects.add(result);
                        newFinalData.setRedisDataList(collects);
                        //新增数据库数据
                        final ResultFinalNotCleanData save = repository.save(newFinalData);
                    }
                    finalData.setPlugsList(finalParams);
                } else {
                    //找出插件编号不同的数据
                    List<SanerPlugsRedisDataParams> collects = FinalData.getRedisDataList().stream().filter(list -> !StringUtils.equals(list.getPlugsNo(), params.getPlugsNo())).collect(Collectors.toList());
                    //插件组编号
                    result.setGroupNo(params.getGroupNo());
                    //插件编号
                    result.setPlugsNo(params.getPlugsNo());
                    //插件的类型名字
                    result.setTypeName(Plugs.getTypeName());
                    //判断插件的数据类型
                    if (StringUtils.equals(Plugs.getTypeName(), PlugsEmus.Wifi.getMsg())) {
                        //解决wife-Name名字
                        if (result.getOut().getOut().getNetworks() != null) {
                            List<Map<String, String>> mapLists = result.getOut().getOut().getNetworks();
                            for (Map<String, String> maps : mapLists) {
                                //清洗wife的数据
                                PlugsFinalParams paramss = new PlugsFinalParams();
                                for (Map.Entry<String, String> entry : maps.entrySet()) {
                                    String str = MD5Utils.decodeUTF8Str(entry.getValue());
                                    entry.setValue(str);
                                    if (StringUtils.equals(entry.getKey(), "Mode")) {
                                        entry.setValue(JWTUtils.getStringId());
                                    }
                                }
                                paramss.setOnlyKey(JWTUtils.getStringId());
                                paramss.setGroupNo(params.getGroupNo());
                                paramss.setPlugsNo(params.getPlugsNo());
                                paramss.setTargetName(maps.get("Name"));
                                paramss.setPlugsMac(maps.get("Address"));
                                paramss.setPlugsName(Plugs.getPlugsName());
                                paramss.setPlugsType(PlugsEmus.Wifi.getMsg());
                                paramss.setTime(LocalDateTime.now());
                                finalParams.add(paramss);
                            }
                        }
                        collects.add(result);
                        FinalData.setRedisDataList(collects);
                        //更新数据库数据
                        final ResultFinalNotCleanData save = repository.save(FinalData);
                    } else if (StringUtils.equals(Plugs.getTypeName(), PlugsEmus.Bluetooth.getMsg())) {
                        //清洗Bluetooth的数据
                        final Out2 out = result.getOut().getOut();
                        if (out != null) {
                            final List<Object> devices = out.getDevices();
                            log.info("数据库为不为空的情况，发现蓝牙的数量==》"+devices.size());
                            for (int i = 0; i < devices.size(); i++) {
                                PlugsFinalParams paramss = new PlugsFinalParams();
                                List<Object> listObj = (List<Object>) devices.get(i);
                                final String BulothMac = (String) listObj.get(0);
                                final String BulothName = (String) listObj.get(1);
                                //final Long BulothNum = (Long)listObj.get(2);
                                paramss.setOnlyKey(JWTUtils.getStringId());
                                paramss.setGroupNo(params.getGroupNo());
                                paramss.setPlugsNo(params.getPlugsNo());
                                paramss.setTargetName(BulothName);
                                paramss.setPlugsMac(BulothMac);
                                paramss.setPlugsName(Plugs.getPlugsName());
                                if(StringUtils.equals(PlugsEmus.Bluetooth.getMsg(),Plugs.getPlugsId().split("/")[1])){
                                    paramss.setPlugsType(PlugsEmus.Bluetooth_Br.getMsg());//br
                                }
                                paramss.setPlugsType(Plugs.getPlugsId().split("/")[1]);//ble
                                paramss.setTime(LocalDateTime.now());
                                finalParams.add(paramss);
                            }
                            log.info("数据库为不为空的情况，发现蓝牙的对象list====>"+finalParams);
                        } else {
                            PlugsFinalParams paramss = new PlugsFinalParams();
                            final Out1 out1 = result.getOut();
                            paramss.setOnlyKey(JWTUtils.getStringId());
                            paramss.setGroupNo(params.getGroupNo());
                            paramss.setPlugsNo(params.getPlugsNo());
                            paramss.setTargetName(out1.getError());
                            paramss.setPlugsName(Plugs.getPlugsName());
                            paramss.setPlugsType(Plugs.getPlugsId().split("/")[1]);
                            paramss.setTime(LocalDateTime.now());
                            finalParams.add(paramss);
                        }
                        collects.add(result);
                        FinalData.setRedisDataList(collects);
                        log.info("数据库为不为空的情况，未整合后的数据==》"+collects);
                        //更新数据库数据
                        final ResultFinalNotCleanData save = repository.save(FinalData);
                    }
                    finalData.setPlugsList(finalParams);
                }
            }
        return ServerResponse.createBySuccess(finalData);
    }
}
