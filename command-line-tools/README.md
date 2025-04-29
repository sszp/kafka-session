# Running a local kafka broker and using the kafka tools to run some basic operations
We will start a kafka docker container on port 9092 (kafka default port). This will start a single kafka broker on your machine.
* Run Kafka in docker: `docker run -d -p 9092:9092 --name kafka apache/kafka`
* Create a topic: `docker exec -t kafka ./opt/kafka/bin/kafka-topics.sh --create --topic topic1 --bootstrap-server localhost:9092`
* Check the new topic configs using: `docker exec -t kafka ./opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic topic1`
* You can increase the number of partitions by running: `docker exec -t kafka ./opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic topic1 --alter --partitions 2`
* Run a produce: `docker exec -it kafka ./opt/kafka/bin/kafka-console-producer.sh --topic topic1 --bootstrap-server localhost:9092`
* In a new terminal window, run a consumer: `docker exec -it kafka ./opt/kafka/bin/kafka-console-consumer.sh --topic topic1 --bootstrap-server localhost:9092`
* Send events by typing anything followed by <Enter> in the producer terminal window
* Check the received events on the consumer side
