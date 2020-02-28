package com.xwl.prisonbreak.michael.strategy.service.impl;

import com.xwl.prisonbreak.michael.strategy.service.UserTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xwl
 * @date 2020-01-20 15:46
 * @description
 */
@Slf4j
@Service
public class SvipTypeServiceImpl implements UserTypeService {
    @Override
    public void process(String msg) {
        System.out.println("超级会员");
    }
}
