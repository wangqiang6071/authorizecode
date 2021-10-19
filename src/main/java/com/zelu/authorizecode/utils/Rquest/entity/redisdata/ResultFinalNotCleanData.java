package com.zelu.authorizecode.utils.Rquest.entity.redisdata;

import com.zelu.authorizecode.entity.params.SanerPlugsRedisDataParams;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

/**
 * 每个插件最后生成的数据
 * @author wangqiang
 * @Date: 2021/9/18 14:25
 */
@Data
@Document(collection = "result_final_not_clean_date")
public class ResultFinalNotCleanData {
    /**
     * 任务编号
     */
    @MongoId
    private String taskNo;

    /**
     * 插件的最终数据
     */
    List<SanerPlugsRedisDataParams>redisDataList;
}
