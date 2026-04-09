package com.sakiprime.PowerfulEmpathy.util;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class QiniuUtil {
    @Value("${qiniu.access-key}")
    private String accessKey;
    @Value("${qiniu.secret-key}")
    private String secretKey;
    @Value("${qiniu.bucket-name}")
    private String bucketName;
    @Value("${qiniu.domain}")
    private String domain;

    // 接收 userId，生成固定文件名
    public String uploadAvatar(MultipartFile file, String userId) throws Exception {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucketName);

        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);

        // ✅ 固定文件名：user_用户ID_avatar.png（覆盖上传）
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String key = "user_" + userId + "_avatar" + suffix;

        uploadManager.put(file.getInputStream(), key, upToken, null, null);
        return domain + "/" + key;
    }
    public String uploadResume(MultipartFile file, String userId) throws Exception {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucketName);

        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);

        // ✅ 固定文件名：user_用户ID_resume.docx（覆盖上传）
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String key = "user_" + userId + "_resume" + suffix;

        uploadManager.put(file.getInputStream(), key, upToken, null, null);
        return domain + "/" + key;
    }
}
