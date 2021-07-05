package life.haiming.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class AliCloudProvider {
    @Value("${ali.accessKey_ID}")
    private String AccessKey_ID;

    @Value("${ali.accessKey_Secret}")
    private String AccessKey_Secret;

    @Value("${ali.bucketName}")
    private String BucketName;

    String endpoint = "oss-cn-beijing.aliyuncs.com";
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
        OSS ossClient = new OSSClientBuilder().build(endpoint, AccessKey_ID, AccessKey_Secret);

        // 创建Bucket(可能不需要手动？)
        //ossClient.createBucket(BucketName);

        ossClient.putObject(BucketName, "pictures/"+generatedFileName, fileStream);




        // 关闭OSSClient。
        ossClient.shutdown();
        return generatedFileName;
    }
}
