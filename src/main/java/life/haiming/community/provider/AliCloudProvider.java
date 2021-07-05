package life.haiming.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import life.haiming.community.exception.CustomizeErrorCode;
import life.haiming.community.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
public class AliCloudProvider {
    @Value("${ali.accessKey_ID}")
    private String accessKeyId;

    @Value("${ali.accessKey_Secret}")
    private String accessKeySecret;

    @Value("${ali.bucketName}")
    private String bucketName;

    @Value("${ali.endpoint}")
    private String endpoint;


    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。

    public String upload(InputStream fileStream, String fileName) {
        String generatedFileName;
        String[] fileNames = fileName.split("\\.");
        if (fileNames.length > 1) {
            // fileNames[fileNames.length - 1] 代表文件的后缀
            generatedFileName = UUID.randomUUID().toString() + "." + fileNames[fileNames.length - 1];
        } else {
            return null;
        }
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建Bucket(可能不需要手动？)
        //ossClient.createBucket(BucketName);

        String key = "pictures/" + generatedFileName;
        PutObjectResult putObjectResult = ossClient.putObject(bucketName, key, fileStream);
        if (putObjectResult != null) {
            //生成url
            // 过期时间：1小时 3600*1000
            Date expiration = new Date(new Date().getTime() + 24 * 3600 * 1000);
            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
            // 关闭OSSClient。
            ossClient.shutdown();
            return url.toString();
        } else {
            // 关闭OSSClient。
            ossClient.shutdown();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FILE);
        }
    }
}
