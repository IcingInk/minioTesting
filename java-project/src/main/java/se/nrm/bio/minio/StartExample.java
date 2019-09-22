/*
 * Copyright (C) 2019 ingimar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package se.nrm.bio.minio;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        final String accessKey = "minio";
        final String secretKey = "minio123";
        final String ipPort = "http://172.19.0.1:9001";

        MinioClient minioClient = new MinioClient(ipPort, accessKey, secretKey);

        final String bucket = "first-bucket";
        boolean isExist = minioClient.bucketExists(bucket);
        if (isExist) {
            System.out.println("Bucket already exists.");
        } else {
            minioClient.makeBucket(bucket);
        }
        // minioClient.listBuckets().forEach(b -> System.out.println(b.name()));

        String object = "20190922-null.jpg";

        //StartExample.storeImageNull2Bucket(minioClient, bucket, object);
        StartExample.retrieveObject(minioClient, bucket, object);
        
        
        // StartExample.checkStatus(minioClient, bucket, object);
        
        System.out.println("End of code");

    }

    // When you start the application you should see a file cat.jpg 
    // in the directory that you specified as the root folder when you started the Minio server.
    // https://docs.min.io/docs/java-client-api-reference.html 
    /*
     * https://docs.min.io/docs/java-client-api-reference.html#putObject 
     */
    // Works to fetch a file that is stored in directory objectToBeSaved ...
    private static void storeImage2Bucket(MinioClient minioClient, String bucket, String objectName) throws IOException {
        System.out.println("Store-method, stores to bucket = ".concat(bucket));
        String objectToBeSaved = "/home/ingimar/repos/icingink-github/minioTesting/tmp/".concat(objectName);
        try {
            minioClient.putObject(bucket, objectName, objectToBeSaved);
            System.out.println("uploaded successfully");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
    }

    /**
     * putObject(String bucketName, String objectName, String fileName, Long
     * size, Map<String,String> headerMap, ServerSideEncryption sse, String
     * contentType)
     *
     * @param minioClient
     * @param bucket
     * @param objectName
     * @throws IOException
     */
    private static void storeImageNull2Bucket(MinioClient minioClient, String bucket, String objectName) throws IOException {
        System.out.println("Fetches an image from Internet Archive and stores that image in a bucket");
        System.out.println("storeStream2Bucket, stores to bucket = ".concat(bucket));

        Path tempFile = Files.createTempFile(objectName, ".jpg");
        URL url;
        try {
            url = new URL("https://archive.org/download/testbild1988x556/TESTBILD-1-988x556.jpg");

            System.out.println("Temp-file is ".concat(tempFile.toString()));
            InputStream inStream = url.openStream();
            Files.copy(inStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            Long lng = null;
            minioClient.putObject(bucket, objectName, tempFile.toString(), lng, null, null, "image/jpeg");

        } catch (Exception ex) {
            Logger.getLogger(StartExample.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * using java 1.7 NIO to copy the filestream 
     * @param minioClient
     * @param bucket
     * @param objectName
     * @throws Exception 
     */
    private static void retrieveObject(MinioClient minioClient, String bucket, String objectName) throws Exception {
        final String path ="/home/ingimar/repos/minio/";
        System.out.println("Retrieve-method , fetch  ".concat(objectName).concat(" and store in ".concat(path)));

        final File targetFile = new File(path.concat(objectName));
        final String FILE_TO = targetFile.toString();
        // java 1.7 NIO.
        try ( InputStream inputStream = minioClient.getObject(bucket, objectName);) {
            Files.copy(inputStream, Paths.get(FILE_TO));
        }
    }

    private static void checkStatus(MinioClient minioClient, String bucket, String objectName) throws Exception {
        ObjectStat statObject = minioClient.statObject(bucket, objectName);
        System.out.println("Statistics " + statObject);
    }
}
