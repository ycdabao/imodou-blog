package com.imodou.blog.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.InputStream;

public class QiniuUtil {

    //...生成上传凭证，然后准备上传
    public static String accessKey = "c_2_TwzQm6ZDFgIgd6JwUa-6rqmv43ma8zgTBLQR";
    public static String secretKey = "qivC1qo-Wzna8KPbXa971niwzFX7XR-CNrm76M0F";
    public static String bucket = "imodou-blog";


    public static void upload(InputStream is, String filename){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filename;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(is,key,upToken,null, null);
            //解析上传成功的结果
     /*       DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);*/
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }
}
