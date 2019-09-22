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

1. in ->  # mc ls minio
2. out <- [2019-09-18 14:37:56 UTC]      0B ingimar-1/

## testing the java minio client library

### running the maven-project
