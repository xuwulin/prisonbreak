package com.xwl.prisonbreak.michael.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xwl.prisonbreak.common.util.FileUtils;
import com.xwl.prisonbreak.common.util.PdfUtils;
import com.xwl.prisonbreak.common.vo.ResponseInfo;
import com.xwl.prisonbreak.common.vo.ResponseResult;
import com.xwl.prisonbreak.exception.BusinessException;
import com.xwl.prisonbreak.michael.entity.FileInfo;
import com.xwl.prisonbreak.michael.service.FileInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Auther: xwl
 * @Date: 2019/6/5 21:13
 * @Description: 文件上传、下载、图片查看
 */
@RestController
@RequestMapping("/api/file")
@Api(tags = "文件上传、下载")
@Slf4j
public class FileInfoController {

    @Autowired
    private FileInfoService fileInfoService;

    /**
     * 文件上传
     * 1. 文件上传后的文件名
     * 2. 上传后的文件路径 , 当前年月日时 如:2018060801  2018年6月8日 01时
     * 3. file po 类需要保存文件信息：文件原名称, 文件大小, 上传时间, 是否删除。。。
     *
     * @param file 文件
     * @return
     */
    @PostMapping("/uploadFile")
    @ApiOperation("单文件文件上传")
    public ResponseInfo<?> uploadFile(@RequestParam("file") MultipartFile file) {
        // 判断文件是否为空
        if (file.isEmpty()) {
            return ResponseInfo.error("文件不能为空");
        }
        try {
            return fileInfoService.upload(file);
        } catch (BusinessException e) {
            return e.getResponse();
        }
    }

    @PostMapping("/uploadFiles")
    @ApiOperation("多文件上传")
    public ResponseInfo<?> uploadFiles(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        if (files == null || files.length <= 0) {
            return ResponseInfo.error("文件不能为空");
        }
        try {
            for (int i = 0; i < files.length; i ++) {
                fileInfoService.upload(files[i]);
            }
            return ResponseInfo.success(null);
        } catch (BusinessException e) {
            return e.getResponse();
        }
    }

    /**
     * 文件下载（根据文件id下载）
     * @param id
     * @param res
     */
    @PostMapping("/downloadFileById/{id}")
    @ApiOperation("文件下载（根据文件id下载）")
    public void downloadFileById(@PathVariable("id") String id, HttpServletResponse res) throws BusinessException {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException("1001", "id不能为空");
        }
        try {
            FileInfo fileInfo = fileInfoService.getById(id);
            if (fileInfo == null) {
                throw new BusinessException("1001", "文件不能为空");
            }
            // 下载
            fileInfoService.downloadFile(fileInfo, res);
        } catch (BusinessException e) {
            e.getResponse();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件下载（根据文件名fileName下载）
     * @param fileName
     * @param res
     */
    @PostMapping("/downloadFileByFileName/{fileName}")
    @ApiOperation("文件下载（根据文件名fileName）")
    public void downloadFileByFileName(@PathVariable("fileName") String fileName, HttpServletResponse res) throws BusinessException {
        if (StringUtils.isBlank(fileName)) {
            throw new BusinessException("1001", "文件名不能为空");
        }
        // 构造查询条件
        LambdaQueryWrapper<FileInfo> queryWrapper = new QueryWrapper<FileInfo>()
                .lambda()
                .eq(FileInfo::getFileName, fileName);
        try {
            // 查询，文件名是16位随机数，重复的可能性很小，但不排除重复的可能
            List<FileInfo> fileInfos = fileInfoService.list(queryWrapper);
            FileInfo fileInfo = fileInfos.get(0);
//            FileInfo fileInfo = fileInfoService.getOne(queryWrapper);
            if (fileInfo == null) {
                throw new BusinessException("1001", "文件不能为空");
            }
            // 下载
            fileInfoService.downloadFile(fileInfo, res);
        } catch (BusinessException e) {
            e.getResponse();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件查看
     */
    @GetMapping("/view")
    @ApiOperation("文件查看")
    public ResponseEntity<InputStreamResource> view(@RequestParam("fileName") String fileName){
        FileInfo fileInfo = null;
        try {
            fileInfo = fileInfoService.getImage(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fileInfo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders header = new HttpHeaders();
        if (FileUtils.match(fileInfo.getFileName(), "jpg", "png", "gif", "bmp", "tif")) {
            header.setContentType(MediaType.IMAGE_JPEG);
        } else if (FileUtils.match(fileInfo.getFileName(), "html", "htm")) {
            header.setContentType(MediaType.TEXT_HTML);
        } else if (FileUtils.match(fileInfo.getFileName(), "pdf")) {
            header.setContentType(MediaType.APPLICATION_PDF);
        } else {
            header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }
        header.add("X-Filename", fileInfo.getFileName());
        header.add("X-MD5", fileInfo.getMd5());

        return new ResponseEntity<>(new InputStreamResource(fileInfo.getContent()), header, HttpStatus.OK);
    }

    /**
     * 文件列表查询
     */
    @GetMapping("/find")
    @ApiOperation("文件列表查询")
    public ResponseInfo<?> findList(@RequestParam("resourceId") String resourceId) {
        try {
            return fileInfoService.findFileList(resourceId);
        }catch (BusinessException e){
            return e.getResponse();
        }
    }

    /**
     * 逻辑删除文件
     */
    @DeleteMapping("/deleteFile")
    @ApiOperation("逻辑删除文件")
    public ResponseInfo<?> deleteFile(@RequestParam("fileName") String fileName) {
        try {
            return fileInfoService.deleteFile(fileName);
        }catch (BusinessException e){
            return e.getResponse();
        }
    }

    @GetMapping("/imageToPdf")
    @ApiOperation("将图片转换为PDF")
    public ResponseResult imageToPdf(String imageDirPath, String pdfDirPath, String pdfFileName) {
        PdfUtils.imagesToPdf(pdfDirPath, pdfFileName, imageDirPath);
        return new ResponseResult("转换成功！");
    }
}
