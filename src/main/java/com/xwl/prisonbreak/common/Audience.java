package com.xwl.prisonbreak.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "audience")
@Component
public class Audience {
    /**
     * 代表这个JWT的接收对象,存入audience
     */
    private String clientId;

    /**
     * 密钥, 用于加密，可通过Base64加密，可自行替换为其他加密方式，本例中使用Base64加密
     */
    private String secret;

    /**
     * JWT的签发主体，存入issuer
     */
    private String name;

    /**
     * 过期时间，时间戳
     */
    private int expiresSecond;
}