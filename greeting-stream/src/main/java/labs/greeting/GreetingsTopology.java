package labs.greeting;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GreetingsTopology {
    private GreetingsTopology() {
    }
    public static Topology build() {
        var streamsBuilder = new StreamsBuilder();

        /**
         * The source stream is created with the topic name and the key and value Serdes.
         */
        var sourceStream = streamsBuilder
            .stream(Topic.GREETINGS.getName(), Consumed.with(Serdes.String(), Serdes.String()));

        /**
         * Merge the greetings stream with the greetings_spanish stream.
         */
        var mergedStreams = sourceStream.merge(
            streamsBuilder.stream(Topic.GREETINGS_SPANISH.getName(),
            Consumed.with(Serdes.String(), Serdes.String())));
        
        /**
         * The processor stream is created by transforming the value to uppercase and then splitting the value by space.
         * The peek method is used to log the transformed value.
         */
        var processorStream = mergedStreams
            .mapValues((key, value) -> value.toUpperCase())
            .flatMapValues((ValueMapper<String, Iterable<String>>) value -> java.util.Arrays.asList(value.split(" ")))
            .peek((key, value) -> log.info("The transformed value is: {}", value));

        /** 
         * The processor stream is sent to the uppercase topic.
         */
        processorStream
            .to(Topic.GREETINGS_UPPERCASE.getName(), Produced.with(Serdes.String(), Serdes.String()));

          return streamsBuilder.build();
    }
}
