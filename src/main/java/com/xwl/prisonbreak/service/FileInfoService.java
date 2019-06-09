package com.xwl.prisonbreak.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xwl.prisonbreak.exception.BusinessException;
import com.xwl.prisonbreak.model.po.FileInfo;
import com.xwl.prisonbreak.model.vo.ResponseInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @Auther: xwl
 * @Date: 2019/6/5 21:09
 * @Description: 文件上传、下载接口
 */
public interface FileInfoService extends IService<FileInfo> {

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws BusinessException
     */
    ResponseInfo<?> upload(MultipartFile file) throws BusinessException;

    /**
     * 文件下载
     *
     * @param fileInfo 文件信息
     * @param res 响应结果
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     */
    void downloadFile(FileInfo fileInfo, HttpServletResponse res) throws BusinessException, UnsupportedEncodingException;

    /**
     * 文件查看
     */
    FileInfo getImage(String fileName) throws IOException;

    /**
     * 根据资源id查询文件信息
     *
     * @param resourceId
     * @return
     * @throws BusinessException
     */
    ResponseInfo<?> findFileList(String resourceId) throws BusinessException;

    /**
     * 逻辑删除文件
     *
     * @param fileName
     * @return
     * @throws BusinessException
     */
    ResponseInfo<?> deleteFile(String fileName) throws BusinessException;

    /**
     * 每天执行一次,清除无效图片
     */
    void deleteValidFalse();
}
