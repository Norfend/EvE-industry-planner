package industry.eve;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ParsingServiceApplication {
    public static void main(String[] args) {
        ParsingService service = new ParsingService(args[0]);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String rawResources = objectMapper.writeValueAsString(service.getRawResources());
            String materialsAfterReprocessing = objectMapper.writeValueAsString(service.getMaterialsAfterReprocessing());
            String reprocessingBlueprints = objectMapper.writeValueAsString(service.getReprocessingBlueprints());
            Files.writeString(Paths.get("ParsingService/output\\RawResources.json"), rawResources);
            Files.writeString(Paths.get("ParsingService/output\\MaterialsAfterReprocessing.json"), materialsAfterReprocessing);
            Files.writeString(Paths.get("ParsingService/output\\ReprocessingBlueprints.json"), reprocessingBlueprints);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
