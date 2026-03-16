package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import org.springframework.beans.factory.annotation.Value;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 通用接口
 */

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传:{}", file);
        // 判断是否传入空文件
        if(!file.isEmpty()) {

            try {
                File dir = new File(uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs(); // 递归创建目录
                }
                // 构建文件名，防止重复
                String originalFileName = file.getOriginalFilename();
                if (originalFileName != null) {
                    String fileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));

                    // 写入本地文件夹中
                    Path path = Paths.get(uploadPath, fileName);
                    file.transferTo(path);

                    return Result.success(path.toString());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // 否则返回错误信息
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
