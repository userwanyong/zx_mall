package com.xixi.user.controller;

import cn.hutool.core.io.FileUtil;
import com.xixi.user.common.AuthAccess;
import com.xixi.user.common.HoneyLogs;
import com.xixi.user.common.LogType;
import com.xixi.user.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author 永
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";
    @Value("${ip:localhost}")
    String ip;
    @Value("${server.port}")
    String port;

    @HoneyLogs(operation = "文件", type = LogType.ADD)
//    @AuthAccess
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();//文件的原始名称

        String mainName = FileUtil.mainName(originalFilename);//aaa
        String extName = FileUtil.extName(originalFilename);
//        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);//png

        if (!FileUtil.exist(ROOT_PATH)) {
            FileUtil.mkdir(ROOT_PATH);//如果当前文件的父级目录不存在，就创建
        }
        if (FileUtil.exist(ROOT_PATH + File.separator + originalFilename)) {//如果当前上传的文件已经存在了，那么这时候要重名一个文件夹名称
            originalFilename = System.currentTimeMillis() + "_" + mainName + "." + extName;
        }
        File saveFile = new File(ROOT_PATH + File.separator + originalFilename);
        file.transferTo(saveFile);//储存文件到本地的磁盘里去
        String url = "http://" + ip + ":" + port + "/file/download/" + originalFilename;
        return Result.success(url);//返回文件的链接，这个链接就是文件的下载地址，这个下载地址就是后台提供出来的
    }


    @AuthAccess
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));//附件下载
        String filePath = ROOT_PATH + File.separator + fileName;
        if (!FileUtil.exist(filePath)) {
            return;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
