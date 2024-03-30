package com.heima.minio.test;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.io.FileInputStream;


public class MinIOTest {


    public static void main(String[] args) {

        try (FileInputStream fileInputStream = new FileInputStream("D:/list.html")) {
            MinioClient minioClient = MinioClient.builder().endpoint("http://192.168.1.10:9000").credentials("minio", "minio123").build();

            PutObjectArgs putObjectArgs = PutObjectArgs.builder().object("list.html").contentType("text/html").bucket("leadnews").
                    stream(fileInputStream, fileInputStream.available(), -1).build();
            minioClient.putObject(putObjectArgs);
            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}