package com.xwl.prisonbreak.michael.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.prisonbreak.michael.mapper.TimedTaskMapper;
import com.xwl.prisonbreak.michael.entity.TimedTask;
import com.xwl.prisonbreak.michael.service.TimedTaskService;
import org.springframework.stereotype.Service;

/**
 * @Auther: xwl
 * @Date: 2019/7/25 17:46
 * @Description:
 */
@Service
public class TimedTaskServiceImpl extends ServiceImpl<TimedTaskMapper, TimedTask> implements TimedTaskService {
}
