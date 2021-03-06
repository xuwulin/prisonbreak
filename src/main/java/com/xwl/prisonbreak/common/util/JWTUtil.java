package com.xwl.prisonbreak.common.util;

import com.xwl.prisonbreak.common.Audience;
import com.xwl.prisonbreak.michael.entity.SysUser;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author xwl
 * @date 2020-03-27 13:52
 * @description
 */
public class JWTUtil {
    private static Logger log = LoggerFactory.getLogger(JWTUtil.class);
    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 创建Jwt，用户登录成功后调用此方法创建
     * 使用Hs256算法  私匙使用用户密码
     * <p>
     * iss(Issuser)：代表这个JWT的签发主体；
     * sub(Subject)：代表这个JWT的主体，即它的所有人；
     * aud(Audience)：代表这个JWT的接收对象；
     * exp(Expiration time)：是一个时间戳，代表这个JWT的过期时间；
     * nbf(Not Before)：是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的；
     * iat(Issued at)：是一个时间戳，代表这个JWT的签发时间；
     * jti(JWT ID)：是JWT的唯一标识。
     *
     * @param userId   用户id
     * @param username 用户名
     * @param role     用户角色
     * @param audience com.xwl.prisonbreak.common.Audience
     * @return
     */
    public static String createJWT(String userId, String username, String role, Audience audience) {
        // 指定签名的时候使用的签名算法，使用HS256加密算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(audience.getSecret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        // 如果需要，可以对userId进行加密
        String encodeUserId = Base64Util.encode(userId);

        // 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);

        // 生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。
        // 它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
//        String key = sysUser.getPassword(); // 这里使用的是用户密码

        // 下面就是在为payload添加各种标准声明和私有声明了
        // 这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                // iat: jwt的签发时间
                .setIssuedAt(now)
                // 代表这个JWT的签发主体
                .setIssuer(audience.getName())
                // 代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setSubject(username)
                // 代表这个JWT的接收对象
                .setAudience(audience.getClientId())
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, signingKey);

        // 添加Token过期时间
        int TTLMillis = audience.getExpiresSecond();
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp)  // 是一个时间戳，代表这个JWT的过期时间；
                    .setNotBefore(now); // 是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的
        }
        // 生成JWT
        return builder.compact();
    }

    /**
     * 解析jwt
     *
     * @param token  加密后的token
     * @param secret 秘钥
     * @return
     */
    public static Claims parseJWT(String token, String secret) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                    .parseClaimsJws(token).getBody();
            return claims;
        } catch (ExpiredJwtException eje) {
            log.error("===== Token过期 =====", eje);
            throw new RuntimeException("Token过期", eje);
        } catch (Exception e) {
            log.error("===== token解析异常 =====", e);
            throw new RuntimeException("token解析异常", e);
        }
    }

    /**
     * 校验token
     * 在这里可以使用官方的校验，我这里校验的是token中携带的密码于数据库一致的话就校验通过
     *
     * @param token
     * @param sysUser
     * @return
     */
    public static Boolean isVerify(String token, SysUser sysUser) {
        // 签名秘钥，和生成的签名的秘钥一模一样
        String key = sysUser.getPassword();

        // 得到DefaultJwtParser
        Claims claims = Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(key)
                // 设置需要解析的jwt
                .parseClaimsJws(token).getBody();

        if (claims.get("password").equals(sysUser.getPassword())) {
            return true;
        }
        return false;
    }

    /**
     * 从token中获取用户ID
     *
     * @param token
     * @param base64Security
     * @return
     */
    public static String getUserId(String token, String base64Security) {
        String userId = parseJWT(token, base64Security).get("userId", String.class);
        return Base64Util.decode(userId);
    }

    /**
     * 从token中获取用户名
     *
     * @param token
     * @param secret
     * @return
     */
    public static String getUsername(String token, String secret) {
        String username = parseJWT(token, secret).get("username", String.class);
        return username;
    }

    /**
     * 是否已过期
     *
     * @param token
     * @param base64Security
     * @return
     */
    public static boolean isExpiration(String token, String base64Security) {
        return parseJWT(token, base64Security).getExpiration().before(new Date());
    }
}
