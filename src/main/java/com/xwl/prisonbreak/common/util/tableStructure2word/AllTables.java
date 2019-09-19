package com.xwl.prisonbreak.common.util.tableStructure2word;



import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: xwl
 * @Date: 2019/9/19 9:16
 * @Description: 表描述信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllTables {
    @ApiModelProperty(name = "tableNameDesc", value = "表名描述")
    private String tableNameDesc;

    @ApiModelProperty(name = "tableName", value = "表名")
    private String tableName;

    @ApiModelProperty(name = "primaryKey", value = "主键（复合主键之间用、连接）")
    private String primaryKey;

    @ApiModelProperty(name = "tables", value = "数据表")
    private List<TableStructure> tables;
}
