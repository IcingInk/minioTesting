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
import io.minio.errors.MinioException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        String bucket = "first-bucket";

        boolean isExist = minioClient.bucketExists(bucket);
        if (isExist) {
            System.out.println("Bucket already exists.");
        } else {
            minioClient.makeBucket(bucket);
        }
       // minioClient.listBuckets().forEach(b -> System.out.println(b.name()));

        String object = "20190922-A.jpg";

        //StartExample.storeToBucket(minioClient, bucket, object); // NOT-OK : does not store the entire file
        // StartExample.store2aBucket(minioClient, bucket, object); // OK: stores the entire file.
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
    private static void storeToBucket(MinioClient minioClient, String bucket, String objectName) throws IOException {
        System.out.println("Store-method, stores to bucket = ".concat(bucket));

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

    // Works to fetch a file that is stored in directory objectToBeSaved ...
    private static void store2aBucket(MinioClient minioClient, String bucket, String objectName) throws IOException {
        System.out.println("Store-method, stores to bucket = ".concat(bucket));
        String objectToBeSaved ="/home/ingimar/repos/icingink-github/minioTesting/tmp/".concat(objectName);
        try {
            minioClient.putObject(bucket, objectName, objectToBeSaved);
            System.out.println("uploaded successfully");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
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

        byte[] buffer = new byte[16384];
        inStream.read(buffer);
        File targetFile = new File("/home/ingimar/repos/minio/".concat(objectName));
       // File targetFile = new File("/tmp/".concat(objectName));
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);

        inStream.close();
        outStream.close();

//        try ( FileOutputStream outputStream = new FileOutputStream(targetFile)) {
//
//            IOUtils.copy(inStream, outputStream);
//
//        }
    }

    private static void checkStatus(MinioClient minioClient, String bucket, String objectName) throws Exception {
        ObjectStat statObject = minioClient.statObject(bucket, objectName);
        System.out.println("Statistics " + statObject);

    }

}
