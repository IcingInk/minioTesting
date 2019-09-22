/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.minio;

import com.amazonaws.util.IOUtils;
import io.minio.MinioClient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Following ->
 * https://golb.hplar.ch/2017/02/Upload-files-from-Java-to-a-Minio-server.html
 *
 * @author ingimar
 */
public class StartExample {

    public static void main(String[] args) throws Exception {

        String accessKey = "minio";
        String secretKey = "minio123";
        String ipPort = "http://172.19.0.1:9001";

        MinioClient minioClient = new MinioClient(ipPort, accessKey, secretKey);

        String bucket = "ingimar-1";

        boolean isExist = minioClient.bucketExists(bucket);
        if (isExist) {
            System.out.println("Bucket already exists.");
        } else {
            minioClient.makeBucket(bucket);
        }
        // denna skriver ut 'ingimar-1' , (write out all my buckets')
        minioClient.listBuckets().forEach(b -> System.out.println(b.name()));

        String object = "20190920-Funk-B.jpg";

        StartExample.storeToBucket(minioClient, bucket, object);
       //  StartExample.retrieveObject(minioClient, bucket, object);
        System.out.println("End of code");

    }

    // When you start the application you should see a file cat.jpg 
    // in the directory that you specified as the root folder when you started the Minio server.
    // https://docs.min.io/docs/java-client-api-reference.html 
    /*
    https://docs.min.io/docs/java-client-api-reference.html#putObject 
    
     */
    private static void storeToBucket(MinioClient minioClient, String bucket, String objectName) throws IOException {
        System.out.println("Store-method ");

        URL url;

        try {
            url = new URL("https://archive.org/download/testbild1988x556/TESTBILD-1-988x556.jpg");
            Path tempFile = Files.createTempFile(objectName, ".jpg");
            Long lng;
            try ( InputStream inStream = url.openStream()) {
                lng = Long.valueOf(inStream.available());
                Files.copy(inStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            }
            System.out.println("Path tempfile is ".concat(tempFile.toString()));
            System.out.println("lng ".concat(lng.toString()));
            minioClient.putObject(bucket, objectName, tempFile.toString(), lng, null, null, "image/jpeg");
            //minioClient.putObject(bucket, objectName, tempFile.toString(), lng, null, null, "binary/octet-stream");

        } catch (Exception ex) {
            Logger.getLogger(StartExample.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // https://howtodoinjava.com/java/io/4-ways-to-copy-files-in-java/
    private static void retrieveObject(MinioClient minioClient, String bucket, String objectName) throws Exception {
        System.out.println("Retrieve  -method ");
        String accessKey = "minio";
        String secretKey = "minio123";
        String ipPort = "http://172.19.0.1:9001";

        MinioClient client = new MinioClient(ipPort, accessKey, secretKey);

        InputStream inStream = client.getObject(bucket, objectName);
//      
//        byte[] buffer = new byte[16384];
//        inStream.read(buffer);
        File targetFile = new File("/home/ingimar/repos/minio/".concat(objectName));
//        OutputStream outStream = new FileOutputStream(targetFile);
//        outStream.write(buffer);
//
//        inStream.close();
//        outStream.close();

        try ( FileOutputStream outputStream = new FileOutputStream(targetFile)) {

            IOUtils.copy(inStream, outputStream);
           
        }

    }

}


