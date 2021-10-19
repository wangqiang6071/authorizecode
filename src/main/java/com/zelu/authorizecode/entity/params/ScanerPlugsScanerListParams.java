package com.zelu.authorizecode.entity.params;

import lombok.Data;

import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/10/14 13:37
 */
@Data
public class ScanerPlugsScanerListParams {
    private String taskNo;
    private List<String> plugsNo;
}
