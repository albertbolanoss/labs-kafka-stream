package labs.greeting.domain;

import java.time.LocalDateTime;

public record Gretting (String message, LocalDateTime timestamp) {    
}
