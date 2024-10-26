package labs.greeting;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "greeting-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        var topology = GreetingsTopology.build();
        var kafkaStreams = new KafkaStreams(topology, properties);

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        
        try {
            kafkaStreams.start();
        } catch (Exception e) {
            kafkaStreams.close();
        }
    }
}