package labs.greeting;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void createKafkaProperties() {
        var properties = Main.createKafkaProperties();

        Assertions.assertNotNull(properties);
        Assertions.assertNotNull(properties.get(StreamsConfig.APPLICATION_ID_CONFIG));
        Assertions.assertNotNull(properties.get(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG));
        Assertions.assertNotNull(properties.get(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG));

    }
}
