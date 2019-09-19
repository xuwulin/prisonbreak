package com.xwl.prisonbreak.michael.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwl.prisonbreak.common.util.tableStructure2word.TableStructure;

import java.util.List;

/**
 * @Author: xwl
 * @Date: 2019/9/19 15:45
 * @Description: 表结构mapper
 */
public interface TableStructureMapper extends BaseMapper<TableStructure> {
    /**
     * 获取所有表结构
     * @return
     */
    List<TableStructure> listTableStructure();
}
