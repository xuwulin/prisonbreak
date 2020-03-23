package com.xwl.prisonbreak.michael.controller;

import com.xwl.prisonbreak.common.vo.ResponseResult;
import com.xwl.prisonbreak.michael.service.SnowflakeUseHutoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xwl
 * @date 2020-03-23 12:14
 * @description
 */
@RestController
@RequestMapping("/api/snowflake")
@Api(tags = "雪花算法id生成器")
public class SnowflakeUseHutoolController {

    @Autowired
    private SnowflakeUseHutoolService snowflakeUseHutoolService;

    @GetMapping("/getIDBySnowflake")
    @ApiOperation(value = "雪花算法id生成器")
    public ResponseResult<List<Long>> getIDBySnowflake() {
        List<Long> idBySnowflake = snowflakeUseHutoolService.getIDBySnowflake();
        return new ResponseResult<>(idBySnowflake);
    }
}
