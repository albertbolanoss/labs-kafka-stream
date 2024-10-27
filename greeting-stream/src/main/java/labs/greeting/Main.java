package labs.greeting;

import labs.greeting.infrastructure.topology.GreetingOperatorTopology;
import labs.greeting.infrastructure.topology.GreetingTopology;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

public class Main {
    private static final GreetingTopology greetingTopology = new GreetingOperatorTopology();
    public static void main(String[] args) {
        var kafkaStreams = createKafkaStream(greetingTopology.build());

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        
        try {
            kafkaStreams.start();
        } catch (Exception e) {
            kafkaStreams.close();
        }
    }

    public static KafkaStreams createKafkaStream(Topology topology) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "greeting-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);

        return new KafkaStreams(topology, properties);
    }
}
