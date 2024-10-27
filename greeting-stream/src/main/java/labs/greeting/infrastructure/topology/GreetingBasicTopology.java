package labs.greeting.infrastructure.topology;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;


import labs.greeting.infrastructure.enumeration.Topic;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GreetingBasicTopology {
    private GreetingBasicTopology() {
    }

    public static Topology build() {
        var streamsBuilder = new StreamsBuilder();

        /**
         * The source stream is created with the topic name and the key and value Serdes.
         */
        var sourceStream = streamsBuilder
            .stream(Topic.GREETINGS.getName());

        /**
         * The processor stream is created by transforming the value to uppercase and then splitting the value by space.
         * The peek method is used to log the transformed value.
         */
        var processorStream = sourceStream
            .peek((key, value) -> log.info("The value to send is: {}", value));

        /** 
         * The processor stream is sent to the uppercase topic.
         */
        processorStream
            .to(Topic.GREETINGS_UPPERCASE.getName());

          return streamsBuilder.build();
    }
}
