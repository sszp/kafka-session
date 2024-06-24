# A simple kafka streams pipeline

A kafka streams application that consumes transfer events and aggregate the total amount by senderId and send the 
resulting aggregates to `stream.processing.output.topic` topic.

To run the service:
* Make sure there is a kafka broker running locally. If using docker, you can run `docker ps` to check if the kafka broker is running. You
  can start it using `docker run -d -p 9092:9092 apache/kafka:3.7.0`.
* Start a command-line consumer to check the output aggregates: `./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic stream.processing.output.topic --from-beginning
  `
* Run `ProducerMain.java` to generate some transfer events, you can run it with gradle: `./gradlew run -PmainClassToRun=com.transferwise.streamprocessing.producer.ProducerMain`
* Start the kafka streams pipeline `PipelineMain.java` , you can run it with gradle: `./gradlew run -PmainClassToRun=com.transferwise.streamprocessing.PipelineMain`
* Check the console consumer window for the resulting aggregates