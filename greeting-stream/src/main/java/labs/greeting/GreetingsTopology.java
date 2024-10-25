package labs.greeting;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

public class GreetingsTopology {
    public static Topology build() {
        var streamsBuilder = new StreamsBuilder();

        streamsBuilder
            .stream(Topic.GREETINGS.getName(), Consumed.with(Serdes.String(), Serdes.String()))
            .mapValues((key, value) -> value.toUpperCase())
            .to(Topic.GREETINGS_UPPERCASE.getName(), Produced.with(Serdes.String(), Serdes.String()));

          return streamsBuilder.build();

    }
}
