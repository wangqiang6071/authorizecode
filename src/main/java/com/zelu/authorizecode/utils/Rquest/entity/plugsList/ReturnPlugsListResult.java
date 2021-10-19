package com.zelu.authorizecode.utils.Rquest.entity.plugsList;

import lombok.Data;
import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/9/16 10:09
 * 获取所有的插件集合列表数据1
 */
@Data
public class ReturnPlugsListResult {
    private String ver;
    private List<ReturnPlugs> modules;
}
