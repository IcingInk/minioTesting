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


# MC

1. https://docs.min.io/docs/minio-server-configuration-guide.html

\# mc stat minio/first-bucket

1. Name      : first-bucket/
2. Date      : 1970-01-01 00:00:00 UTC 
3. Size      : 0 B    
4. Type      : folder

\# mc ls minio/first-bucket

1. [2019-09-22 19:12:02 UTC]   16KiB 20190920-Funk-B.jpg


# troubleshooting
the file that is stored is saved in /tmp ( now = 20190920-Funk-B.jpg12165863578734466712.jpg )  and the file that is retrieved from the minio-server is stored in /tmp (20190920-Funk-B.jpg)

1. /tmp> ll *jpg
2. 8520775 -rw-rw-r-- 1 ingimar ingimar  16384 sep 22 21:49 20190920-Funk-B.jpg
3. 8520781 -rw-rw-r-- 1 ingimar ingimar 131986 sep 22 21:49 20190920-Funk-B.jpg12165863578734466712.jpg

So the original size is 129K in the /tmp-directory , the retrieved file is only 16K.

the file stored in the mino-bucket is 16KiB :

1. \# mc ls minio/first-bucket
2. [2019-09-22 19:12:02 UTC]   16KiB 20190920-Funk-B.jpg 


