package open.tabletop.generation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Config {
    private String model;
    private String openAiAPIKey;
}
