package com.xwl.prisonbreak.michael.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xwl.prisonbreak.common.util.resttemplate.RestTemplateUtils;
import com.xwl.prisonbreak.common.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xwl
 * @date 2019-11-08 10:41
 * @description
 */
@RestController
@RequestMapping("/api/restTemplateTest")
@Api(tags = "restTemplateUtils测试")
public class RestTemplateUtilsController {

    @GetMapping("testRestTemplateUtilsGetHttpWithParam")
    @ApiOperation("测试RestTemplateUtils中的getHttp方法，带参")
    public ResponseResult testRestTemplateUtilsGetHttpWithParam() {
        String url = "http://localhost:8080/prisonbreak/api/user/getPage";

        // 封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("page", 2);
        map.put("pageSize", 5);
        JSONObject param = JSON.parseObject(JSONObject.toJSONString(map));

        String result = RestTemplateUtils.getHttp(url, param, 3000, 3000, 3);
        return new ResponseResult("OK", result);
    }

    @GetMapping("testRestTemplateUtilsGetHttpWithNotParam")
    @ApiOperation("测试RestTemplateUtils中的getHttp方法，不带参")
    public ResponseResult testRestTemplateUtilsGetHttpWithNotParam() {
        String url = "http://localhost:8080/prisonbreak/api/user/findByIds";
        JSONObject param = new JSONObject();

        String result = RestTemplateUtils.getHttp(url, param, 3000, 3000, 3);
        return new ResponseResult("OK", result);
    }

    @PostMapping("testRestTemplateUtilsPostHttpWithParam")
    @ApiOperation("测试RestTemplateUtils中的postHttp方法，带参")
    public ResponseResult testRestTemplateUtilsPostHttpWithParam() {
        String url = "http://localhost:8080/prisonbreak/api/user/insertWithName";

        // 封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("username", "秦可卿");
        map.put("nickname", "兼美");
        JSONObject param = JSON.parseObject(JSONObject.toJSONString(map));

        // 封装header信息
        Map<String, Object> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=UTF-8");

        String result = RestTemplateUtils.postHttp(url, param, header, 3000, 3000, 3);
        return new ResponseResult("OK", result);
    }

    @PostMapping("testRestTemplateUtilsPostHttpWithNotParam")
    @ApiOperation("测试RestTemplateUtils中的postHttp方法，不带参")
    public ResponseResult testRestTemplateUtilsPostHttpWithNotParam() {
        String url = "http://localhost:8080/prisonbreak/api/user/hello";

        // 封装参数，没有参数直接new JSONObject();
        JSONObject param = new JSONObject();
        // 封装header信息
        Map<String, Object> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=UTF-8");

        String result = RestTemplateUtils.postHttp(url, param, header, 3000, 3000, 3);
        return new ResponseResult("OK", result);
    }

    @PostMapping("testRestTemplateUtilsPostHttpEncryption")
    @ApiOperation("测试RestTemplateUtils中的postHttpEncryption方法，加密参数类型请求：application/x-www-form-urlencoded")
    public ResponseResult testRestTemplateUtilsPostHttpEncryption() {
        String url = "http://192.168.20.193:8080/api/v1/users";

        // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("org_name", "Shanghai");
        paramMap.add("username", "ZhangSan");

        // 封装header信息
        Map<String, Object> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");

        // 加密参数类型请求  application/x-www-form-urlencoded
        String result = RestTemplateUtils.postHttpEncryption(url, paramMap, header, 3000, 3000, 3);
        return new ResponseResult("OK", result);
    }

    @GetMapping("testRestTemplateUtilsexchangeForGet")
    @ApiOperation("测试RestTemplateUtils中的exchange方法，发送GET请求")
    public ResponseResult testRestTemplateUtilsexchangeForGet() {
        String url = "http://192.168.20.193:8080/api/v1/query";
        String tokenStr = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzMyMjM4NTUsInVzZXJuYW1lIjoidGlhbnFpIiwib3JnTmFtZSI6IlNoYW5naGFpIiwiaWF0IjoxNTczMTg3ODU1fQ.nQR-YvHTVZqjauTQce92L9yRXzifgYVrHFw1c_ZpJqE";

        // 参数封装
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("org_name", "Shanghai");
        paramMap.put("item_id", "[\"a7f167fbab4bda4dcf87f20d515083814865ba032f070aa6a4ae4087457e64d0\"]");

        // 封装header信息
        Map<String, Object> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=UTF-8");
        header.put("Authorization", "Bearer " + tokenStr);

        String result = RestTemplateUtils.exchange(url, HttpMethod.GET, paramMap, header, 5000, 5000, 3);
        return new ResponseResult("OK", result);
    }

    @PostMapping("testRestTemplateUtilsexchangeForPost")
    @ApiOperation("测试RestTemplateUtils中的exchange方法，发送POST请求")
    public ResponseResult testRestTemplateUtilsexchangeForPost() {
        String url = "http://localhost:8080/prisonbreak/api/user/insertWithName";

        // 参数封装
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", "秦可卿");
        paramMap.put("nickname", "兼美");

        // 封装header信息
        Map<String, Object> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=UTF-8");

        String result = RestTemplateUtils.exchange(url, HttpMethod.POST, paramMap, header, 3000, 3000, 3);
        return new ResponseResult("OK", result);
    }

    @PutMapping("testRestTemplateUtilsexchangeForPut")
    @ApiOperation("测试RestTemplateUtils中的exchange方法，发送PUT请求")
    public ResponseResult testRestTemplateUtilsexchangeForPut() {
        String url = "http://192.168.20.193:8080/api/v1/lifecycle";
        String tokenStr = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzMyMjM4NTUsInVzZXJuYW1lIjoidGlhbnFpIiwib3JnTmFtZSI6IlNoYW5naGFpIiwiaWF0IjoxNTczMTg3ODU1fQ.nQR-YvHTVZqjauTQce92L9yRXzifgYVrHFw1c_ZpJqE";

        // 参数封装
        Map<String, Object> map = new HashMap<>();
        map.put("item_id", "a8f167fbab4bda4dcf87f20d515083814865ba032f070aa6a4ae4087457e64d0");
        map.put("description", "上海公证处公证员张三于 2019-05-10 15:46:12 查阅原件");
        List<Map<String, Object>> list = Arrays.asList(map);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("org_name", "Shanghai");
        paramMap.put("lifecycle_data", list);

        // 封装header信息
        Map<String, Object> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=UTF-8");
        header.put("Authorization", "Bearer " + tokenStr);

        String result = RestTemplateUtils.exchange(url, HttpMethod.PUT, paramMap, header, 3000, 3000, 3);
        return new ResponseResult("OK", result);
    }

    @DeleteMapping("testRestTemplateUtilsexchangeForDelete")
    @ApiOperation("测试RestTemplateUtils中的exchange方法，发送DELETE请求")
    public ResponseResult testRestTemplateUtilsexchangeForDelete() {
        String url = "http://localhost:8080/prisonbreak/api/user/delByIdAndNickName";

        // 参数封装
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", 109);
        paramMap.put("nickName", "兼美");

        // 封装header信息
        Map<String, Object> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=UTF-8");

        String result = RestTemplateUtils.exchange(url, HttpMethod.DELETE, paramMap, header, 3000, 3000, 3);
        return new ResponseResult("OK", result);
    }

}
