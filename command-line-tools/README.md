# Running a local kafka broker and using the kafka tools to run some basic operations
We will start a kafka docker container on port 9092 (kafka default port). We will use the separately downloaded tools 
to connect to the kafka broker and run commands. An alternative to downloading the tools is it use the kafka tools included 
in the kafka docker image using something like `docker exec -it <CONTAINER ID> ./opt/kafka/bin/kafka-topics.sh --create --topic topic1 --bootstrap-server localhost:9092`
* Download: https://downloads.apache.org/kafka/3.7.0/kafka_2.13-3.7.0.tgz
* tar -xzf kafka_2.13-3.7.0.tgz
* docker run -d -p 9092:9092 apache/kafka:3.7.0
* cd kafka_2.13-3.7.0/bin
* kafka-topics.sh --create --topic topic1 --bootstrap-server localhost:9092
* In a new terminal window, run: kafka-console-consumer.sh --topic topic1  --bootstrap-server localhost:9092
* kafka-console-producer.sh --topic topic1 --bootstrap-server localhost:9092
* Send events by typing anything followed by enter
* Check the received events on the consumer side
