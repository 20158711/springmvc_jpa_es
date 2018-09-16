package org.wingstudio.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wingstudio.service.IFileService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements IFileService {
    @Override
    public String upload(MultipartFile file, String path) {
        String name = file.getOriginalFilename();
        String extension=name.substring(name.lastIndexOf("."));
        String uploadName= UUID.randomUUID().toString()+extension;
        log.info("开始上传文件{},路径为{}",name,uploadName);
        File fileDir=new File(path);
        if (!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile=new File(path,uploadName);
        try {
            file.transferTo(targetFile);
        }catch (IOException e){
            log.error("上传文件异常{}",e);
            return null;
        }
        return uploadName;
    }
}
