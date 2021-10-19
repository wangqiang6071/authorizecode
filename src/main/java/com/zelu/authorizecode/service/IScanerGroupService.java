package com.zelu.authorizecode.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zelu.authorizecode.common.ServerResponse;
import com.zelu.authorizecode.entity.ScanerGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import net.sf.jsqlparser.statement.create.table.Index;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * <p>
 * 插件组 服务类
 * </p>
 *
 * @author wangqiang
 * @since 2021-09-16
 */
public interface IScanerGroupService {
    //添加更新插件组
    public ServerResponse<String> add_update_plug_group(ScanerGroup grop);

    //删除插件组
    public ServerResponse<String> delete_plug_group(String gropNo);

    //获取所有的插件组列表
    public ServerResponse<List<ScanerGroup>> plugs_group_list();
}
