package com.xwl.prisonbreak.common.util.lfasr;

import com.alibaba.fastjson.JSON;
import com.iflytek.msp.cpdb.lfasr.exception.LfasrException;
import com.iflytek.msp.cpdb.lfasr.model.LfasrType;
import com.iflytek.msp.cpdb.lfasr.model.Message;
import com.iflytek.msp.cpdb.lfasr.model.ProgressStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * @author xwl
 * @date 2020-04-17 9:36
 * @description 语音转写工具类
 */
@Slf4j
public class LfasrUtil {

    /**
     * 原始音频存放地址
     */
    private static final String local_file = "src/main/resources/audio/news.mp3";

    /**
     * 转写类型选择：标准版和电话版(旧版本, 不建议使用)分别为：
     * LfasrType.LFASR_STANDARD_RECORDED_AUDIO：16K标准版\-已录制音频，支持格式wav,flac,opus,mp3,m4a
     * 和 LfasrType.LFASR_TELEPHONY_RECORDED_AUDIO
     *
     */
    private static final LfasrType type = LfasrType.LFASR_STANDARD_RECORDED_AUDIO;

    /**
     * 等待时长（秒）
     */
    private static int sleepSecond = 20;

    public static void main(String[] args) throws Exception {
        File file = new File(local_file);
        List<LfasrResDTO> lfasrResDTOS = lfasrResult(file);
        System.out.println(lfasrResDTOS);
    }

    public static List<LfasrResDTO> lfasrResult(File file) throws Exception {
        // 初始化LFASRClient实例，返回转写客户端对象
        MyLfasrClientImp lc = null;
        try {
            lc = MyLfasrClientImp.initLfasrClient();
        } catch (LfasrException e) {
            // 初始化异常，解析异常描述信息
            Message initMsg = JSON.parseObject(e.getMessage(), Message.class);
            // getErr_no()：字符串类型，错误码，getFailed()：字符串类型，错误码描述
            log.error("初始化异常，异常描述信息==>ecode：" + initMsg.getErr_no() + "，failed：" + initMsg.getFailed());
            throw new Exception("ecode：" + initMsg.getErr_no() + "，failed：" + initMsg.getFailed(), e);
        }

        // 获取上传任务ID
        String task_id = "";
        HashMap<String, String> params = new HashMap<>();
        // has_participle：转写结果是否包含分词信息，false或true， 默认false
        params.put("has_participle", "false");
        // 转写结果中是否包含发音人分离信息，false或true，标准版默认：false，电话版默认：true，合并后标准版开启电话版功能
        // params.put("has_seperate", "true");
        try {
            // 上传音频文件，返回消息对象
            Message uploadMsg = lc.lfasrUpload(file, type, params);

            // 判断返回值，int类型：-1表示失败、 0表示成功
            int ok = uploadMsg.getOk();
            if (ok == 0) {
                // 创建任务成功
                // 成功时携带返回值（上传接口返回任务id字符串，获取进度接口返回进度状态JSON，获取结果接口返回转写结果JSON）
                task_id = uploadMsg.getData();
                log.info("任务task_id：" + task_id);
            } else {
                // 创建任务失败-服务端异常
                log.info("创建任务失败-服务端异常==>ecode：" + uploadMsg.getErr_no() + "，failed：" + uploadMsg.getFailed());
            }
        } catch (LfasrException e) {
            // 上传异常，解析异常描述信息
            Message uploadMsg = JSON.parseObject(e.getMessage(), Message.class);
            log.error("上传异常，解析异常描述信息==>ecode：" + uploadMsg.getErr_no() + "，failed：" + uploadMsg.getFailed());
            throw new Exception("上传异常，解析异常描述信息==>ecode：" + uploadMsg.getErr_no() + "，failed：" + uploadMsg.getFailed(), e);
        }

        // 循环等待音频处理结果
        while (true) {
            try {
                // 等待20s在获取任务进度
                log.info("转换中 ...");
                Thread.sleep(sleepSecond * 1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                throw new Exception(e);
            }
            try {
                // 获取处理进度，返回消息对象
                Message progressMsg = lc.lfasrGetProgress(task_id);

                // 如果返回状态不等于0，则任务失败
                if (progressMsg.getOk() != 0) {
                    log.info("task was fail. task_id:" + task_id);
                    log.info("返回状态不等于0，任务失败==>ecode：" + progressMsg.getErr_no() + "，failed：" + progressMsg.getFailed());
                    return null;
                } else {
                    ProgressStatus progressStatus = JSON.parseObject(progressMsg.getData(), ProgressStatus.class);
                    if (progressStatus.getStatus() == 9) {
                        // 处理完成
                        log.info("处理完成. task_id:" + task_id);
                        break;
                    } else {
                        // 未处理完成
                        log.info("未处理完成. task_id:" + task_id + ", status:" + progressStatus.getDesc());
                        continue;
                    }
                }
            } catch (LfasrException e) {
                // 获取进度异常处理，根据返回信息排查问题后，再次进行获取
                Message progressMsg = JSON.parseObject(e.getMessage(), Message.class);
                log.error("获取进度异常处理，根据返回信息排查问题后，再次进行获取==>ecode：" + progressMsg.getErr_no() + "，failed：" + progressMsg.getFailed());
                throw new Exception("获取进度异常处理，根据返回信息排查问题后，再次进行获取==>ecode：" + progressMsg.getErr_no() + "，failed：" + progressMsg.getFailed(), e);
            }
        }

        // 获取任务结果
        try {
            // 返回消息对象
            Message resultMsg = lc.lfasrGetResult(task_id);
            // 如果返回状态等于0，则获取任务结果成功
            if (resultMsg.getOk() == 0) {
                // 打印转写结果
                log.info("转写结果：" + resultMsg.getData());
                List<LfasrResDTO> lfasrResDTOS = JSON.parseArray(resultMsg.getData(), LfasrResDTO.class);
                // 返回结果
                return lfasrResDTOS;
            } else {
                // 获取任务结果失败
                log.info("获取任务结果失败==>ecode：" + resultMsg.getErr_no() + "failed：" + resultMsg.getFailed());
            }
        } catch (LfasrException e) {
            // 获取结果异常处理，解析异常描述信息
            Message resultMsg = JSON.parseObject(e.getMessage(), Message.class);
            log.error("获取结果异常处理，解析异常描述信息==>ecode：" + resultMsg.getErr_no() + "failed：" + resultMsg.getFailed());
            throw new Exception("获取结果异常处理，解析异常描述信息==>ecode：" + resultMsg.getErr_no() + "failed：" + resultMsg.getFailed(), e);
        }
        return null;
    }
}
