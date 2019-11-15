package com.enbuys.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Pace
 * @Data: 2019/11/15 16:52
 * @Version: v1.0
 */
public class FileUpload {

    public static Map<String,Object> upload(MultipartFile file,String fileSpace,String uploadPathDB) throws Exception{
        String fileFinalPath = null;
        // 判断文件是否存在
        if(file != null){
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;

            // 获取文件名
            String fileName = file.getOriginalFilename();
            if(!StringUtils.isEmpty(fileName)){
                // 拼接最终路径
                fileFinalPath = fileSpace + uploadPathDB + "/" + fileName;
                // 设置数据库保存的路径
                uploadPathDB += ("/" + fileName);

                File saveFile = new File(fileFinalPath);
                // 判断路径中父文件夹是否存在，不存在创建
                if(saveFile.getParentFile() != null || !saveFile.getParentFile().isDirectory()){
                    saveFile.getParentFile().mkdirs();
                }
                // 存在，保存到本地
                try {
                    fileOutputStream = new FileOutputStream(saveFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if(fileOutputStream!=null){
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }
            }else {
                return null;
            }
        }else {
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("fileFinalPath",fileFinalPath);
        map.put("uploadPathDB",uploadPathDB);
        return map;
    }
}
