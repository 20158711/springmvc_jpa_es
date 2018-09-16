package org.wingstudio.controller.backend;


import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.service.IFileService;
import org.wingstudio.service.IUserService;
import org.wingstudio.util.PropertiesUtil;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/file")
@Api(tags = "文件传输")
public class FileTransferController {

    @Autowired
    private IFileService fileService;

    @Autowired
    private IUserService userService;

    @ApiOperation("文件上传")
    @ApiImplicitParam(paramType = "query",name = "session",defaultValue = "")
    @RequestMapping(value = "/upload.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(
            @RequestParam(value = "file",required = false) MultipartFile file,
            HttpSession session){
        ServerResponse serverResponse = userService.checkAdmin(session);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }
        if (file==null)
            return ServerResponse.errorMessage("文件不能为空");
        String path=session.getServletContext().getRealPath("upload");
        String targetFileName=fileService.upload(file,path);
        if (targetFileName == null) {
            return ServerResponse.errorMessage("上传文件失败");
        }
        String url= PropertiesUtil.getProperty("fileServer.prefix")+targetFileName;
        Map fileMap=Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);
        return ServerResponse.success(fileMap);
    }

//    @ApiOperation("富文件本图片上传")
//    @RequestMapping(value = "/richtext_img_upload.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ServerResponse richTextImgUpload(@RequestParam(value = "file",required = false) MultipartFile file, HttpSession session){
//        //TODO 根据富文本要求返回
//    }

}
