package com.xwl.prisonbreak.michael.strategy.service.impl;

import com.xwl.prisonbreak.michael.strategy.service.UserTypeService;
import org.springframework.stereotype.Service;

/**
 * @author xwl
 * @date 2020-01-20 15:46
 * @description
 */
@Service
public class SsvipTypeServiceImpl implements UserTypeService {
    @Override
    public void process(String msg) {
        System.out.println("至尊会员");
    }
}
