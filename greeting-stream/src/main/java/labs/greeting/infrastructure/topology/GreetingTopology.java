package labs.greeting.infrastructure.topology;

import org.apache.kafka.streams.Topology;

public interface GreetingTopology {
    Topology build();
}
