package com.xwl.prisonbreak.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/16 10:28
 * @Description: mybatis-plus配置公共字段自动填充
 * 配置公共字段自动填充功能  @TableField(..fill = FieldFill.INSERT)
 * 特别注意，3.0-gamma之前的版本 MetaObjectHandler 是抽象类
 * 3.0-RC之后的版本MetaObjectHandler 是接口
 */
@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建时间
        Object createTime = getFieldValByName("createTime", metaObject);
        // 修改时间
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (createTime == null) {
            // mybatis-plus版本2.0.9+
            setFieldValByName("createTime", new Date(), metaObject);
        }
        if (updateTime == null) {
            // mybatis-plus版本2.0.9+
            setFieldValByName("updateTime", new Date(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (updateTime == null) {
            // mybatis-plus版本2.0.9+
            setFieldValByName("updateTime", new Date(), metaObject);
        }

    }
}
