package com.heima.minio.test;

import com.heima.file.service.FileStorageService;
import com.heima.minio.MinIOApplication;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest(classes = MinIOApplication.class)
@RunWith(SpringRunner.class)
public class MinIOTest {

    @Autowired
    private FileStorageService fileStorageService;

    public static void main(String[] args) {

        try (FileInputStream fileInputStream = new FileInputStream("E:\\BaiduNetdiskDownload\\day02-app端文章查看，静态化freemarker,分布式文件系统minIO\\资料\\模板文件\\plugins\\js\\index.js")) {
            MinioClient minioClient = MinioClient.builder().endpoint("http://192.168.1.8:9000").credentials("minio", "minio123").build();

            PutObjectArgs putObjectArgs = PutObjectArgs.builder().object("plugins/js/index.js").contentType("text/javascript").bucket("leadnews").
                    stream(fileInputStream, fileInputStream.available(), -1).build();
            minioClient.putObject(putObjectArgs);
            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testUpload() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("E:\\BaiduNetdiskDownload\\day02-app端文章查看，静态化freemarker,分布式文件系统minIO\\资料\\模板文件\\plugins\\css\\index.css");
        String path = fileStorageService.uploadHtmlFile("", "list.html", fileInputStream);
        System.out.println(path);

    }

}