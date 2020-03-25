package com.xwl.prisonbreak.michael.strategy;

import com.xwl.prisonbreak.common.util.SpringBeanFactory;
import com.xwl.prisonbreak.michael.strategy.service.UserTypeService;
import com.xwl.prisonbreak.michael.strategy.service.impl.CommonTypeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xwl
 * @date 2020-01-20 16:03
 * @description 获取 UserTypeService 对应的实现类，关键！！！
 */
@Component
@Slf4j
public class UserTypeContext {
    /**
     * 获取执行器实例
     *
     * @param command 执行器实例
     * @return
     */
    public UserTypeService getInstance(String command) {

        Map<String, String> allClazz = UserLevelEnum.getAllClazz();

        String trim = command.trim();
        // 获取全类名
        String clazz = allClazz.get(trim);
        UserTypeService userTypeService = null;
        try {
            if (StringUtils.isEmpty(clazz)) {
                // 如果为空，则返回通用实现类
                clazz = CommonTypeServiceImpl.class.getName();
            }
            // Class.forName(clazz)：通过反射获取该类的实例
            // 从 Spring Bean 容器中获取一个 InnerCommand 实例
            userTypeService = (UserTypeService) SpringBeanFactory.getBean(Class.forName(clazz));
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return userTypeService;
    }
}
