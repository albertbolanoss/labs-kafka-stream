package labs.greeting;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GreetingsTopology {
    private GreetingsTopology() {
    }
    public static Topology build() {
        var streamsBuilder = new StreamsBuilder();

        streamsBuilder
            .stream(Topic.GREETINGS.getName(), Consumed.with(Serdes.String(), Serdes.String()))
            .mapValues((key, value) -> value.toUpperCase())
            .peek((key, value) -> log.info("The transformed value is: {}", value))
            .to(Topic.GREETINGS_UPPERCASE.getName(), Produced.with(Serdes.String(), Serdes.String()));

          return streamsBuilder.build();

    }
}
