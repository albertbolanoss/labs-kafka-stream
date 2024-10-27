package labs.greeting.infrastructure.topology;

import labs.greeting.TestHelper;
import labs.greeting.infrastructure.enumeration.Topic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GreetingOperatorTopologyTest {
    private TopologyTestDriver testDriver;
    private TestInputTopic<String, String> greetingInputTopic;
    private TestInputTopic<String, String> greetingSpanishInputTopic;
    private TestOutputTopic<String, String> upperOutputTopic;

    @BeforeEach
    void setup() {

        var testHelper = new TestHelper();
        var greetingTopology = new GreetingOperatorTopology();
        var props = testHelper.getKafkaStreamsConfig();

        testDriver = new TopologyTestDriver(greetingTopology.build(), props);

        greetingInputTopic = testDriver.createInputTopic(
                Topic.GREETINGS.getName(), Serdes.String().serializer(), Serdes.String().serializer());

        greetingSpanishInputTopic = testDriver.createInputTopic(
                Topic.GREETINGS_SPANISH.getName(), Serdes.String().serializer(), Serdes.String().serializer());

        upperOutputTopic = testDriver.createOutputTopic(
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

        greetingInputTopic.pipeInput(KEY1, "Hello world");
        greetingSpanishInputTopic.pipeInput(KEY2, "Good bye world");

        TestRecord<String, String> record1 = upperOutputTopic.readRecord();
        assertEquals(KEY1, record1.getKey());
        assertEquals("HELLO", record1.getValue());

        TestRecord<String, String> record2 = upperOutputTopic.readRecord();
        assertEquals(KEY1, record2.getKey());
        assertEquals("WORLD", record2.getValue());

        TestRecord<String, String> record3 = upperOutputTopic.readRecord();
        assertEquals(KEY2, record3.getKey());
        assertEquals("GOOD", record3.getValue());

        TestRecord<String, String> record4 = upperOutputTopic.readRecord();
        assertEquals(KEY2, record4.getKey());
        assertEquals("BYE", record4.getValue());

        TestRecord<String, String> record5 = upperOutputTopic.readRecord();
        assertEquals(KEY2, record5.getKey());
        assertEquals("WORLD", record5.getValue());
    }
}
