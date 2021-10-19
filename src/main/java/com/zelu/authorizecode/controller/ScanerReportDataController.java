package com.zelu.authorizecode.controller;

import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.emus.PlugsEmus;
import com.zelu.authorizecode.entity.*;
import com.zelu.authorizecode.entity.params.*;
import com.zelu.authorizecode.exceptions.StringException;
import com.zelu.authorizecode.service.IScanerReportDataService;
import com.zelu.authorizecode.utils.JWTUtils;
import com.zelu.authorizecode.utils.Rquest.entity.RedisParams;
import com.zelu.authorizecode.utils.Rquest.entity.RequestModel;
import com.zelu.authorizecode.utils.Rquest.entity.plugsList.ReturnPlugsListResult;
import com.zelu.authorizecode.utils.Rquest.entity.plugsdate.ReturnPlugsDate;
import com.zelu.authorizecode.utils.Rquest.entity.redisdata.ResultFinalFuctionData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  任务中的插件扫描数据 前端控制器
 * </p>
 * @author wangqiang
 * @since 2021-09-16
 */
@Slf4j
@RestController
@RequestMapping("/report_data")
public class ScanerReportDataController {
    @Autowired
    @Lazy
    private IScanerReportDataService reportDataService;
    @Autowired
    private MongoTemplate repository;
    /**
     * 获取所有插件列表
     */
    @PostMapping("plugs_list")
    public ServerResponse<ReturnPlugsListResult> get_plugs_list(@RequestBody RequestModel model){
        return reportDataService.plugs_list_data(model);
    }

    /**
     * 获取插件运行之后的的task_id
     */
    @PostMapping("task_id")
    public ServerResponse<ReturnPlugsDate> get_plugs_task_id(@RequestBody RequestModel model){
        return reportDataService.plugs_data(model);
    }

    /**
     * 获取插件的原生数据接口
     */
    @PostMapping("plugs_data")
    public ServerResponse<SanerPlugsRedisDataParams> get_final_redis_date(@RequestBody RedisParams params){
        return reportDataService.redis_date(params);
    }


    //==================================================================================

    /**
     * fuctin插件获取数据的接口  单个fuction插件扫描
     */
    @PostMapping("plugs_fuction_data")
    public ServerResponse<SanerPlugsRedisDataParams> plugs_fuction_data(@RequestBody RedisParams params){
        return reportDataService.redis_fuction_data(params);
    }


    //多个base插件一块扫描
    @PostMapping("plugs_scaners")//SanerPlugsRedisDataParams
    public ServerResponse<SancerFinalCleanListData> plugs_base_scaners(@RequestBody ScanerPlugsScanerListParams params){
        if(params.getPlugsNo().size()==0){
            return ServerResponse.createByError("参数不能为空");
        }
        final ScanerTask task = repository.findById(params.getTaskNo(), ScanerTask.class);
        if(task==null){
            return ServerResponse.createByError("任务编号不存在");
        }
        //最后的数据
        SancerFinalCleanListData dataList=new SancerFinalCleanListData();
        dataList.setTaskNo(params.getTaskNo());
        for(String param:params.getPlugsNo()){
            final ScanerPlugs Plugs = repository.findById(param, ScanerPlugs.class);
            if(Plugs==null){
                throw new StringException("传入的插件编号有误");
            }
            if(!StringUtils.equals(Plugs.getType(),PlugsEmus.Base.getMsg())){
                throw new StringException("传入的插件编号非基础插件编号");
            }
                //查询插件的参数
                final ScanerPlugsParams plugsParams = repository.findById(param, ScanerPlugsParams.class);
                    RequestModel model=new RequestModel();
                    model.setFunc("method");
                    model.setParams(plugsParams.getParams());
                    //任务的redis-task_id
                    final String task_id = reportDataService.plugs_data(model).getData().getTask_id();
                    log.info(String.format("插件的task_id:%s,插件名字:%s,插件类型:%s",task_id,plugsParams.getPlugsName(),plugsParams.getType()));
                    //单个插件所需的参数
                    final RedisParams redisParams = new RedisParams();
                    redisParams.setTaskNo(params.getTaskNo());
                    redisParams.setPlugsNo(param);
                    redisParams.setTaskId(task_id);
                //插件扫描后的结果单个date
                final SancerFinalCleanData data = reportDataService.redis_clean_date(redisParams).getData();
                final String type = Plugs.getPlugsId().split("/")[1];
            //根据任务编号去查询
                if(StringUtils.equals(type,PlugsEmus.Wifi.getMsg())){
                    dataList.setWifeList(data.getPlugsList());
                }else if(StringUtils.equals(type,PlugsEmus.Bluetooth.getMsg())){//br
                    dataList.setBrList(data.getPlugsList());
                }else if(StringUtils.equals(type,"ble")){
                    dataList.setBleList(data.getPlugsList());
                }
            }
        final SancerFinalCleanListData CleanListData = repository.findById(params.getTaskNo(), SancerFinalCleanListData.class);
        if(CleanListData!=null){
            log.info("=========+>1");
            if(CleanListData.getBrList()!=null){
                log.info("=========+>2");
                if(CleanListData.getBrList().size()>0){
                    log.info("=========+>3");
                    if(dataList.getBrList()!=null){
                        log.info("=========+>4");
                        CleanListData.getBrList().clear();
                        if(dataList.getBrList().size()>0){
                            log.info("=========+>5");
                            CleanListData.setBrList(dataList.getBrList());
                        }
                    }
                }else{
                    log.info("=========+>6");
                    if(dataList.getBrList()!=null){
                        log.info("=========+>7");
                        if(dataList.getBrList().size()>0){
                            log.info("=========+>8");
                            CleanListData.setBrList(dataList.getBrList());
                        }
                    }
                }
            }else{
                log.info("=========+>9");
                if(dataList.getBrList()!=null){
                    log.info("=========+>10");
                    if(dataList.getBrList().size()>0){
                        log.info("=========+>11");
                        CleanListData.setBrList(dataList.getBrList());
                    }
                }
            }

            if(CleanListData.getWifeList()!=null){
                log.info("=========+>12");
                if(CleanListData.getWifeList().size()>0){
                    log.info("=========+>13");
                    if(dataList.getWifeList()!=null){
                        log.info("=========+>14");
                        CleanListData.getWifeList().clear();
                        if(dataList.getWifeList().size()>0){
                            log.info("=========+>15");
                            CleanListData.setWifeList(dataList.getWifeList());
                        }
                    }
                }else{
                    log.info("=========+>16");
                    if(dataList.getWifeList()!=null){
                        log.info("=========+>17");
                        if(dataList.getWifeList().size()>0){
                            log.info("=========+>18");
                            CleanListData.setBrList(dataList.getBrList());
                        }
                    }
                }
            }else{
                log.info("=========+>19");
                if(dataList.getWifeList()!=null){
                    log.info("=========+>20");
                    if(dataList.getWifeList().size()>0){
                        log.info("=========+>21");
                        CleanListData.setBrList(dataList.getBrList());
                    }
                }
            }

            if(CleanListData.getBleList()!=null){
                log.info("=========+>22");
                if(CleanListData.getBleList().size()>0){
                    log.info("=========+>23");
                    if(dataList.getBleList()!=null){
                        log.info("=========+>24");
                        CleanListData.getBleList().clear();
                        if(dataList.getBleList().size()>0){
                            log.info("=========+>25");
                            CleanListData.setBleList(dataList.getBleList());
                        }
                    }
                }else{
                    log.info("=========+>26");
                    if(dataList.getBleList()!=null){
                        log.info("=========+>27");
                        if(dataList.getBleList().size()>0){
                            log.info("=========+>28");
                            CleanListData.setBleList(dataList.getBleList());
                        }
                    }
                }
            }else{
                log.info("=========+>29");
                if(dataList.getBleList()!=null){
                    log.info("=========+>30");
                    if(dataList.getBleList().size()>0){
                        log.info("=========+>31");
                        CleanListData.setBleList(dataList.getBleList());
                    }
                }
            }
            log.info("=========+>32");
            repository.save(CleanListData);
        }else{
            log.info("=========+>33");
            repository.save(dataList);
        }
        log.info("=========+>34");
        return ServerResponse.createBySuccess(dataList);
    }

    //1 根据任务taskNo去查询对应的数据
    @PostMapping("plugs_result")
    public ServerResponse<SancerFinalCleanListData> plugs_result(String taskNo){
        final ScanerTask task = repository.findById(taskNo, ScanerTask.class);
        if(task==null){
            return ServerResponse.createByError("任务编号不存在");
        }
        final SancerFinalCleanListData byId = repository.findById(taskNo, SancerFinalCleanListData.class);
        if(byId==null){
            return ServerResponse.createBySuccess("当前任务下暂无基础数据");
        }
        return ServerResponse.createBySuccess(byId);
    }

    //2 根据唯一值查询扫描后的结果onlyKey
    @GetMapping("scaners_result")
    public ServerResponse<ResultFinalFuctionData> scaners_result(String onlyKey){
        final ResultFinalFuctionData task = repository.findById(onlyKey, ResultFinalFuctionData.class);
        return ServerResponse.createBySuccess(task);
    }

    //3 添加基础数据
    @PostMapping("add_base_data")
    public ServerResponse<String> add_plugs(@RequestBody ScanerPlugsTarget target){
        final ScanerTask task = repository.findById(target.getTaskNo(), ScanerTask.class);
        if(task==null){
            return ServerResponse.createByError("传入的任务编号不存在");
        }
        PlugsFinalParams params=new PlugsFinalParams();
        params.setOnlyKey(JWTUtils.getStringId());
        params.setPlugsMac(target.getTargetMac());
        params.setPlugsName(target.getPlugsName());
        params.setPlugsType(target.getTargetType());
        params.setFactoryName(target.getFactoryName());
        params.setTime(LocalDateTime.now());
        final SancerFinalCleanListData dateList = repository.findById(target.getTaskNo(), SancerFinalCleanListData.class);
        if(dateList==null){
            final SancerFinalCleanListData newdateList =new SancerFinalCleanListData();
            if(StringUtils.equals(PlugsEmus.Wifi.getMsg(),target.getTargetType())){
                List<PlugsFinalParams>paramsList=new ArrayList<>();
                paramsList.add(params);
                newdateList.setWifeList(paramsList);
                repository.save(newdateList);
            }else if(StringUtils.equals(PlugsEmus.Bluetooth.getMsg(),target.getTargetType())){//br
                List<PlugsFinalParams>paramsList=new ArrayList<>();
                paramsList.add(params);
                newdateList.setBrList(paramsList);
                repository.save(newdateList);
            }else if(StringUtils.equals(target.getTargetType(),"ble")){
                List<PlugsFinalParams>paramsList=new ArrayList<>();
                paramsList.add(params);
                newdateList.setBleList(paramsList);
                repository.save(newdateList);
            }
        }else{
            if(StringUtils.equals(PlugsEmus.Wifi.getMsg(),target.getTargetType())){
                if(dateList.getWifeList()!=null){
                    final List<PlugsFinalParams> wifeList = dateList.getWifeList();
                    wifeList.add(params);
                    dateList.setWifeList(wifeList);
                    repository.save(dateList);
                }else{
                    List<PlugsFinalParams> wifeList =new ArrayList<>();
                    wifeList.add(params);
                    dateList.setWifeList(wifeList);
                    repository.save(dateList);
                }
            }else if(StringUtils.equals(PlugsEmus.Bluetooth.getMsg(),target.getTargetType())){//br
                if(dateList.getBrList()!=null){
                    final List<PlugsFinalParams> brList = dateList.getBrList();
                    brList.add(params);
                    dateList.setBrList(brList);
                    repository.save(dateList);
                }else{
                    List<PlugsFinalParams> brList =new ArrayList<>();
                    brList.add(params);
                    dateList.setBrList(brList);
                    repository.save(dateList);
                }
            }else if(StringUtils.equals(target.getTargetType(),"ble")){
                if(dateList.getBleList()!=null){
                    final List<PlugsFinalParams> bleList = dateList.getBleList();
                    bleList.add(params);
                    dateList.setBleList(bleList);
                    repository.save(dateList);
                }else{
                    List<PlugsFinalParams> bleList =new ArrayList<>();
                    bleList.add(params);
                    dateList.setBleList(bleList);
                    repository.save(dateList);
                }
            }
        }
        return ServerResponse.createBySuccess("添加成功");
    }

    //查基础库中对应插件类型的数据
    @GetMapping("select_base_data")
    public ServerResponse add_plugs(String taskNo,String plugsType){
        if(StringUtils.isBlank(taskNo)){
            return ServerResponse.createByError("任务编号不能为空");
        }
        if(StringUtils.isBlank(plugsType)){
            return ServerResponse.createByError("插件编号不能为空");
        }
        final ScanerTask byId = repository.findById(taskNo, ScanerTask.class);
        if(byId==null){
            return ServerResponse.createBySuccess("任务编号不存在");
        }
        final SancerFinalCleanListData DataList = repository.findById(taskNo, SancerFinalCleanListData.class);
        if(DataList!=null){
            if(StringUtils.equals(plugsType,PlugsEmus.Wifi.getMsg())){
                return ServerResponse.createBySuccess(DataList.getWifeList());
            }else if(StringUtils.equals(plugsType,PlugsEmus.Bluetooth.getMsg())){
                return ServerResponse.createBySuccess(DataList.getBrList());
            }else if(StringUtils.equals(plugsType,"ble")){
                return ServerResponse.createBySuccess(DataList.getBleList());
            }
        }
        return ServerResponse.createBySuccess("暂无基础数据");
    }
}
