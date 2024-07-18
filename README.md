# PictureService
Micro service node, that provide uploading and cropping pictures with web interface.

![screenshot](https://github.com/ickee953/PictureService/assets/152408327/3bf91b01-c952-4055-872c-96c9a158f56b)

# Quick Start
First of all you need to deploy [StorageService](https://github.com/ickee953/StorageService), because PictureService use it storage-api to store pictures, as files on independent server. You can clone StorageService and use docker to deploy storage-api as docker container:
  1. $ git clone https://github.com/ickee953/StorageService.git
  2. $ cd StorageService
  3. $ mvn clean package
  4. $ docker build -t storage-api . && docker run storage-api

After that you also need to run postgresql with seettings from db-compose-env.yaml:

  5. $ docker-compose -f db-compose-env.yaml up

PictureService has [Apache Kafka](https://kafka.apache.org/quickstart) service that can produce and consume file from external service nodes like [ItemService](https://github.com/ickee953/ItemService) to store it with storage-api. Apache Kafka can be started using ZooKeeper. To get started with either configuration follow below.

Download, extract from [here](https://www.apache.org/dyn/closer.cgi?path=/kafka/3.7.1/kafka_2.13-3.7.1.tgz) and run the following commands in order to start all services in the correct order:

  6. $ bin/zookeeper-server-start.sh config/zookeeper.properties

Open another terminal session and run:

  7. $ bin/kafka-server-start.sh config/server.properties

Then build and run spring boot PictureService application from terminal with standart commands:

  8. $ mvn clean package
  9. $ java -jar target/picture_service-1.0-SNAPSHOT.jar
      --server.port=8081
      --service.storage.url.base=http://localhost:8080
      --spring.datasource.url=jdbc:postgresql://localhost:5433/pictures-api

  10. Open browser at http://localhost:8081
