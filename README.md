# minioTesting
Testing out different minio clients ; this repos sets up the minio-server, has a minio-client (mc) &amp; a java minio client

## start the server 

docker-compose up

## start the  minio-client (mc)

1. ./start_docker_client.sh

configure <p>

1. then use the mc config command.
2. mc stores all its configuration information in ~/.mc/config.json
3. mc config host add <ALIAS> <YOUR-S3-ENDPOINT> <YOUR-ACCESS-KEY> <YOUR-SECRET-KEY> --api <API-SIGNATURE> --lookup <BUCKET-LOOKUP-TYPE>
example:
4. example :  mc config host add minio http://192.168.1.51 BKIKJAA5BMMU2RHO6IBB V7f1CwQqAcwo80UEIJEjc5gVQUSSx5ohQ9GSrr12
5. my settings: mc config host add minio http://172.19.0.1:9001 minio minio123 

Verify <p>
1.  cat /root/.mc/config.json 

    "minio": {
			"url": "http://172.19.0.1:9001",
			"accessKey": "minio",
			"secretKey": "minio123",
			"api": "s3v4",
			"lookup": "auto"
		}, 

the config-file  should have the 'minio'-section


Create a bucket <p>


**A. create a bucket 'bucket' **

1. in -> # mc mb minio/ingimar-1
2. out <- Bucket created successfully `minio/ingimar-1`. 

**B. check all buckets: **

1. in ->  # mc ls minio/ingimar-1
2. out <- [2019-09-18 14:37:56 UTC]      0B ingimar-1/

## testing the java minio client library
pre-req : start the minio-server 

**To be able to run this client **

  * check through the class and look through the hardcoded attributes (IP and Port)
  * The java-class has only one main-method 
  
**methods; run first store and then retrieve ...**

1. void storeToBucket(MinioClient minioClient, String bucket, String objectName)
2. void retrieveObject(MinioClient minioClient, String bucket, String objectName)

### running the maven-project


0. **mvn** clean
1. **mvn** package
2. **java** -jar target/MinioClient.jar


# mc , the minio client and 

1. https://docs.min.io/docs/minio-server-configuration-guide.html 

see the below command 'mc stat' , gives you info on the file as (1) size and (2) Content-Type

\# mc stat minio/first-bucket/20190922-null.jpg

  * Name      : 20190922-null.jpg
  * Date      : 2019-09-22 21:35:12 UTC 
  * Size      : 129 KiB 
  * ETag      : 758c2ff78a4737d12703bd69255d57e8-1 
  * Type      : file 
  * Metadata  :
  *  Content-Type: image/jpeg


# look into:
 
**source code for the client:** <p>

https://github.com/minio/minio-java/blob/master/api/src/main/java/io/minio/MinioClient.java 

**Examples: ** <p>

https://github.com/minio/minio-java/tree/master/examples 


