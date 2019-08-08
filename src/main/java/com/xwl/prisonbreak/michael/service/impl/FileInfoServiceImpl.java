package com.xwl.prisonbreak.michael.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.prisonbreak.config.upload.UploadConfigure;
import com.xwl.prisonbreak.exception.BusinessException;
import com.xwl.prisonbreak.michael.mapper.FileInfoMapper;
import com.xwl.prisonbreak.michael.entity.FileInfo;
import com.xwl.prisonbreak.common.vo.ResponseInfo;
import com.xwl.prisonbreak.michael.service.FileInfoService;
import com.xwl.prisonbreak.common.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: xwl
 * @Date: 2019/6/5 21:10
 * @Description:
 */
@Slf4j
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Resource
    private UploadConfigure uploadConfigure;

    @Transactional
    @Override
    public ResponseInfo<?> upload(MultipartFile file) {
        // 基础路径：D:/prisonbreak-upload/file/
        String basePath = uploadConfigure.getBasePath();
        // 获取文件保存路径：当前日期字符串\当前时间字符串\（\20190605\220512\）
        String folder = FileUtils.getFolder();
        // 获取前缀为"FN-" 长度为19 的文件名：FL_eUljOejPseMeDg86h.png
        String fileName = FileUtils.getFileName("FN-", 16)
                + FileUtils.getFileNameSub(file.getOriginalFilename());
        // 文件夹路径
        Path directoryPath = null;
        // 文件路径
        Path filePath = null;

        try {
            // 创建文件夹
            // D:\prisonbreak-upload\file\20190605\220512\
            directoryPath = Files.createDirectories(Paths.get(basePath, folder));
            log.info("path01-->{}", directoryPath);

            // 获取文件的全路径，如：D:\prisonbreak-upload\file\20190608\154000\FL_bKwUFKrMRyYkHhXX4.txt
            filePath = Paths.get(basePath, folder, fileName);
            log.info("fullPath-->{}", filePath);
            // 写入操作（将文件保存到磁盘指定的位置：filePath）
            Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE);

            // 保存文件信息到数据库
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileOriginName(file.getOriginalFilename());
            fileInfo.setFileType(file.getContentType());
            // 文件大小，单位B（Byte）
            fileInfo.setSize(file.getSize());
            // 文件路径取md5
            fileInfo.setMd5(FileUtils.md5File(filePath.toString()));
            fileInfo.setFileName(fileName);
            fileInfo.setFilePath(directoryPath.toString());

            fileInfoMapper.insert(fileInfo);
        } catch (Exception e) {
            log.error("写入文件异常,删除文件及所在文件夹。。。", e);
            try {
                // Deletes a file if it exists.
                // If the file is a directory then the directory must be empty
                // 1、先删除文件
                Files.deleteIfExists(filePath);
                // 2、再删除文件夹
                Files.deleteIfExists(directoryPath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return ResponseInfo.error(e.getMessage());
        }
        return ResponseInfo.success("上传成功");
    }

    @Override
    public void downloadFile(FileInfo fileInfo, HttpServletResponse res) throws BusinessException, UnsupportedEncodingException {
        if (fileInfo == null) {
            throw new BusinessException("1001", "文件不能为空");
        }

        log.info("fileInfo-->{}", fileInfo);
        if (fileInfo == null) {
            throw new BusinessException("2001", "文件名不存在");
        }

        // 设置响应头
        res.setContentType("application/force-download"); // 设置强制下载不打开
        // 设置下载文件的名称，解决中文乱码
        res.addHeader("Content-Disposition", "attachment;fileName=" +
                new String(fileInfo.getFileOriginName().getBytes("gbk"), "iso8859-1"));
        res.setHeader("Context-Type", "application/xmsdownload");

        // 判断文件是否存在
        File file = new File(Paths.get(fileInfo.getFilePath(), fileInfo.getFileName()).toString());
        if (file.exists()) {
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = res.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                log.info("下载成功");
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("9999", e.getMessage());
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public FileInfo getImage(String fileName) throws IOException {
        log.info("fileName-->{}", fileName);
        FileInfo fileInfo = fileInfoMapper.findByFileNameAndValid(fileName, true);
        if (fileInfo == null) {
            return null;
        }
        Path path = Paths.get(fileInfo.getFilePath(), fileInfo.getFileName());
        log.info("path-->{}", path);
        fileInfo.setContent(Files.newInputStream(path));
        return fileInfo;
    }

    @Override
    public ResponseInfo<?> findFileList(String resourceId) throws BusinessException {
        if (resourceId == null) {
            throw new BusinessException("1001", "资源id不能为空");
        }
        // 根据资源id查询文件信息
        return ResponseInfo.success(fileInfoMapper.findByResourceId(resourceId));
    }

    @Override
    public ResponseInfo<?> deleteFile(String fileName) throws BusinessException {
        if (fileName == null) {
            throw new BusinessException("1001", "文件名不能为空");
        }
        FileInfo fileInfo = fileInfoMapper.findByFileName(fileName);
        if (fileInfo == null) {
            throw new BusinessException("2001", "文件名:" + fileName + " 不存在");
        }
        // 逻辑删除文件
        fileInfo.setDeleted(1);
//        fileInfo.setDeleteTime(DateUtils.getDateString(new Date()));
        // 设置了逻辑删除，所以调用删除方法时就是逻辑删除
        fileInfoMapper.deleteById(fileInfo.getId());
        return ResponseInfo.success(fileInfo);
    }

    @Scheduled(cron = "0 0 0 1/1 * ? ")
    @Override
    public void deleteValidFalse() {
        // 定时删除无效图片信息
        List<FileInfo> fileInfos = fileInfoMapper.findByValid(false);
        List<String> idList = new ArrayList<>();
        fileInfos.forEach(fileInfo -> idList.add(fileInfo.getId()));
        fileInfoMapper.deleteBatchIds(idList);
        log.info("本次删除数据:{}", fileInfos);
    }
}
