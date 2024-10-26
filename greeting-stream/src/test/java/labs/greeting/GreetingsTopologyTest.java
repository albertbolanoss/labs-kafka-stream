package labs.greeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import labs.greeting.infrastructure.GreetingsTopology;

class GreetingsTopologyTest {

    private TopologyTestDriver testDriver;
    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> outputTopic;

    @BeforeEach
    void setup() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        Topology topology = GreetingsTopology.build();
        testDriver = new TopologyTestDriver(topology, props);

        inputTopic = testDriver.createInputTopic(Topic.GREETINGS.getName(), Serdes.String().serializer(), Serdes.String().serializer());
        outputTopic = testDriver.createOutputTopic(Topic.GREETINGS_UPPERCASE.getName(), Serdes.String().deserializer(), Serdes.String().deserializer());
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void testGreetingsTopology() {
        inputTopic.pipeInput("key1", "hello world");
        inputTopic.pipeInput("key2", "hola mundo");

        TestRecord<String, String> record1 = outputTopic.readRecord();
        assertEquals("HELLO", record1.getValue());
        assertEquals("key1", record1.getKey());

        TestRecord<String, String> record2 = outputTopic.readRecord();
        assertEquals("WORLD", record2.getValue());
        assertEquals("key1", record2.getKey());

        TestRecord<String, String> record3 = outputTopic.readRecord();
        assertEquals("HOLA", record3.getValue());
        assertEquals("key2", record3.getKey());

        TestRecord<String, String> record4 = outputTopic.readRecord();
        assertEquals("MUNDO", record4.getValue());
        assertEquals("key2", record4.getKey());
    }
}
