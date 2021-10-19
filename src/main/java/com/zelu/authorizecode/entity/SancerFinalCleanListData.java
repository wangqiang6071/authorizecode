package com.zelu.authorizecode.entity;

import com.zelu.authorizecode.entity.params.PlugsFinalParams;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/10/16 09:37
 */
@Data
@Document(collection = "scaner_final_clean_list_data")
public class SancerFinalCleanListData {
    @MongoId
    private String taskNo;

    private List<PlugsFinalParams> wifeList;
    private List<PlugsFinalParams> brList;
    private List<PlugsFinalParams> bleList;
}
