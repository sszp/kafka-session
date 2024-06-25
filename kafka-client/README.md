A small project that runs a producer and a consumer based on the apache kafka client library.

To run the producer and the consumer:
* Make sure there is a kafka broker running locally. If using docker, you can run `docker ps` to check if the kafka broker is running. You 
can start it using `docker run -d -p 9092:9092 apache/kafka:3.7.0`.
* Create the test topic, this can be skipped if the `auto.create.topics.enable` is set to true (default). To creat the topic 
run `docker exec -it <CONTAINER ID> ./opt/kafka/bin/kafka-topics.sh --create --topic transfers.topic.v1 --partitions 1 --bootstrap-server localhost:9092`
* To start the producer, run `./gradlew :producer:run`
* To start the consumer, run `./gradlew :consumer:run`
* Alternatively you can run both from intellij (`ProducerMain.java` and `ConsumerMain.java`)