package labs.greeting.infrastructure.topology;

import labs.greeting.TestHelper;
import labs.greeting.infrastructure.enumeration.Topic;
import labs.greeting.infrastructure.topology.GreetingBasicTopology;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GreetingBasicTopologyTest {
    private TopologyTestDriver testDriver;
    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> outputTopic;

    @BeforeEach
    void setup() {

        var testHelper = new TestHelper();
        var greetingTopology = new GreetingBasicTopology();
        var props = testHelper.getKafkaStreamsConfig();

        testDriver = new TopologyTestDriver(greetingTopology.build(), props);

        inputTopic = testDriver.createInputTopic(
                Topic.GREETINGS.getName(), Serdes.String().serializer(), Serdes.String().serializer());
        outputTopic = testDriver.createOutputTopic(
                Topic.GREETINGS_UPPERCASE.getName(), Serdes.String().deserializer(), Serdes.String().deserializer());
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void testGreetingsTopology() {
        var KEY1 = "key1";
        var KEY2 = "key2";
        var VALUE1 = "Hello world";
        var VALUE2 = "Good bye world";

        inputTopic.pipeInput(KEY1, VALUE1);
        inputTopic.pipeInput(KEY2, VALUE2);

        TestRecord<String, String> record1 = outputTopic.readRecord();
        assertEquals(KEY1, record1.getKey());
        assertEquals(VALUE1, record1.getValue());

        TestRecord<String, String> record2 = outputTopic.readRecord();
        assertEquals(KEY2, record2.getKey());
        assertEquals(VALUE2, record2.getValue());
    }
}