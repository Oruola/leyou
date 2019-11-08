package com.leyou.upload.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.upload.config.OSSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 该类的作用:
 */
@Service
public class UploadService {
    @Autowired
    private OSSProperties prop;

    @Autowired
    private OSS client;

    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg", "image/bmp");
    public String upload(MultipartFile file) throws IOException {
        String type = file.getContentType();
        if(!suffixes.contains(type)){
            throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
        }
        BufferedImage image = null;
        image= ImageIO.read(file.getInputStream());
        if(image==null){
            throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
        }



        File dir = new File("F:\\RunExe\\nginx-1.13.12\\html\\pic");
        if(!dir.exists()){
            dir.mkdir();
        }
        file.transferTo(new File(dir,file.getOriginalFilename()));
        try {
            return "http://image.leyou.com/pic/"+file.getOriginalFilename();
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }
    }

    public Map<String, String> getSignature() {
        try {
            long expireTime = prop.getExpireTime();
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, prop.getMaxFileSize());
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, prop.getDir());

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<>();
            //***********注意！这里一个坑，前端接收的参数是accessId，demo默认是accessid
            respMap.put("accessId", prop.getAccessKeyId());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", prop.getDir());
            respMap.put("host", prop.getHost());
            //***********注意！这是第二个坑！demo的修改上传时间，比较是毫秒不能除1000*********
            respMap.put("expire", String.valueOf(expireEndTime));
            return respMap;
        }catch (Exception e){
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }
}
