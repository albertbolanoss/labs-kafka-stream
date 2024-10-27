package labs.greeting.infrastructure.topology;

import labs.greeting.infrastructure.enumeration.Topic;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.Optional;

@Slf4j
public class GreetingOperatorTopology implements GreetingTopology {
    public Topology build() {
        var streamsBuilder = new StreamsBuilder();

        /**
         * The source stream is created with the topic name and the key and value Serdes.
         */
        var sourceStream = streamsBuilder
                .stream(Topic.GREETINGS.getName());

        /**
         * Merge the greetings stream with the greetings_spanish stream.
         */
        var mergedStreams = sourceStream.merge(
                streamsBuilder.stream(Topic.GREETINGS_SPANISH.getName()));

        /**
         * The processor stream is created by transforming the value to uppercase and then splitting the value by space.
         * The peek method is used to log the transformed value.
         */
        var processorStream = mergedStreams
                .filter((key, value) -> Optional.ofNullable(value).isPresent())
                .mapValues((key, value) -> String.valueOf(value).toUpperCase())
                .flatMapValues((ValueMapper<String, Iterable<String>>) value -> java.util.Arrays.asList(value.split(" ")))
                .peek((key, value) -> log.info("The transformed value is: {}", value));

        /**
         * The processor stream is sent to the uppercase topic.
         */
        processorStream
                .to(Topic.GREETINGS_UPPERCASE.getName());

        return streamsBuilder.build();
    }
}
