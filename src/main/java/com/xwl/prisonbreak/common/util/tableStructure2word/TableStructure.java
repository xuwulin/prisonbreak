package com.xwl.prisonbreak.common.util.tableStructure2word;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: xwl
 * @Date: 2019/9/19 9:20
 * @Description: 表结构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableStructure {
    @ApiModelProperty(name = "tableName", value = "表名")
    private String tableName;

    @ApiModelProperty(name = "tableNameDesc", value = "表名描述")
    private String tableNameDesc;

    @ApiModelProperty(name = "column", value = "列")
    private String column;

    @ApiModelProperty(name = "columnName", value = "列名")
    private String columnName;

    @ApiModelProperty(name = "columnType", value = "字段类型")
    private String columnType;

    @ApiModelProperty(name = "primary", value = "是否主键：Y/N")
    private String primary;

    @ApiModelProperty(name = "notnull", value = "是否可为空:Y/N")
    private String notnull;

    @ApiModelProperty(name = "defaultVal", value = "默认值")
    private String defaultVal;
}
