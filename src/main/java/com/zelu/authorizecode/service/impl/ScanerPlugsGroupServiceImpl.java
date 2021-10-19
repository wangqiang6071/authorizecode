package com.zelu.authorizecode.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerGroup;
import com.zelu.authorizecode.entity.ScanerPlugs;
import com.zelu.authorizecode.entity.ScanerPlugsGroup;
import com.zelu.authorizecode.entity.params.SanerPlugsParams;
import com.zelu.authorizecode.service.IScanerPlugsGroupService;
import com.zelu.authorizecode.utils.Rquest.entity.redisdata.ResultFinalNotCleanData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 插件与插件组的关系 服务实现类
 * </p>
 * @author wangqiang
 * @since 2021-09-16
 */
@Service
@Slf4j
public class ScanerPlugsGroupServiceImpl implements IScanerPlugsGroupService {
    @Autowired
    private MongoTemplate repository;


    @Override
    public ServerResponse<String> add_update_plug_group(ScanerPlugsGroup plugsGroup) {
        if(StringUtils.isBlank(plugsGroup.getGroupNo())){
           return ServerResponse.createByError("插件组的编号不能为空");
        }
        //检查一下插件组编号是否正常
        final ScanerGroup Group = repository.findById(plugsGroup.getGroupNo(), ScanerGroup.class);
        if(Group==null){
           return ServerResponse.createByError("组编号不存在");
        }
        final List<SanerPlugsParams> plugsNoList = plugsGroup.getPlugsNoList();
        if(plugsNoList.size()<=0){
            return ServerResponse.createByError("插件组传入的插件不能为空");
        }

        for(SanerPlugsParams params:plugsNoList){
            final ScanerPlugs plugs = repository.findById(params.getPlugsNo(), ScanerPlugs.class);
            if(plugs==null){
                return ServerResponse.createByError("插件中的插件编号错误");
            }
        }
        plugsGroup.setCreateTime(LocalDateTime.now());
        final ScanerPlugsGroup save = repository.save(plugsGroup);
        if(save==null){
            return ServerResponse.createByError("创建插件组与插件集合失败");
        }
        return ServerResponse.createBySuccess("创建插件组与插件集合成功");
    }

    @Override
    public ServerResponse<String> delete_plug_group(String groupNo) {
        if(StringUtils.isBlank(groupNo)){
            return ServerResponse.createByError("组编号为空");
        }
        //查询一下组建的数据是否存在
        final ResultFinalNotCleanData scanerReportData = repository.findById(groupNo, ResultFinalNotCleanData.class);
        if(scanerReportData!=null){
            return ServerResponse.createByError("当前任务组下已存在数据无法删除");
        }
        Criteria criteria = new Criteria();
        criteria.andOperator(criteria.where("groupNo").is(groupNo));
        Query query = new Query(criteria);
        final DeleteResult remove = repository.remove(query, ScanerPlugsGroup.class);
        if(remove.getDeletedCount()==0){
            return ServerResponse.createByError("任务组删除失败");
        }
        return ServerResponse.createBySuccess("任务组删除成功");
    }

    //根据插件的编号查询插件组的详情
    @Override
    public ServerResponse<List<SanerPlugsParams>> get_plug_group_details(String groupNo) {
        if(StringUtils.isBlank(groupNo)){
            return ServerResponse.createByError("传入的插件组编号不能为空");
        }
        final ScanerGroup group = repository.findById(groupNo, ScanerGroup.class);
        if(group==null){
            return ServerResponse.createByError("传入的插件组编号错误");
        }
        //查询一下组中的插件数据
        final ScanerPlugsGroup plugsGroup = repository.findById(groupNo, ScanerPlugsGroup.class);
        return ServerResponse.createBySuccess(plugsGroup.getPlugsNoList());
    }

    public static void main(String[] args) {
//        List<SanerPlugsParams> plugsNoList = new ArrayList<>();
//        SanerPlugsParams params1=new SanerPlugsParams();
//        params1.setPlugsOrder(50);
//        SanerPlugsParams params2=new SanerPlugsParams();
//        params2.setPlugsOrder(21);
//        SanerPlugsParams params3=new SanerPlugsParams();
//        params3.setPlugsOrder(40);
//        plugsNoList.add(params1);
//        plugsNoList.add(params2);
//        plugsNoList.add(params3);
//        plugsNoList=plugsNoList.stream().sorted(Comparator.comparing(SanerPlugsParams::getPlugsOrder)).collect(Collectors.toList());
//        log.info("plugsNoList==>"+plugsNoList);
    }
}
