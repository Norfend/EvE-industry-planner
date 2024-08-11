package RefineryService.service;

import RefineryService.model.RawResources;
import RefineryService.model.ReprocessingBlueprint;
import RefineryService.repository.RawResourcesRepository;
import RefineryService.repository.ReprocessingBlueprintRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class RefineryCalculator {
    private final RawResourcesRepository rawResourcesRepository;
    private final ReprocessingBlueprintRepository reprocessingBlueprintRepository;

    @Builder
    private static class Line {
        String item;
        int quantity;
    }

    public List<String> calculator(String inputLines) {
        List<Line> lines = inputLinesParser(inputLines);
        List<String> outputList = new LinkedList<>();
        List<Optional<RawResources>> resources = new LinkedList<>();
        Set<ReprocessingBlueprint> materialsSet = new HashSet<>();
        for (Line line : lines) {
            Optional<RawResources> resource = rawResourcesRepository.findByRawResourceName(line.item);
            if (resource.isPresent() && resource.get().getRawResourcePortionSize() <= line.quantity) {
                List<ReprocessingBlueprint> reprocessingList = reprocessingBlueprintRepository
                                            .findByRawResourceId(resource.get())
                                            .orElseThrow();
                resources.add(resource);
            }
        }
        return outputList;
    }

    private List<Line> inputLinesParser(String inputLines) {
        List<Line> outputLines = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(inputLines);
            for (JsonNode node : jsonNode) {
                Optional<JsonNode> itemOptional = Optional.ofNullable(node.get("item"));
                Optional<JsonNode> quantityOptional = Optional.ofNullable(node.get("quantity"));
                String item = itemOptional.isPresent() ? itemOptional.get().asText("item NOT found")
                        : "item NOT found";
                int quantity = quantityOptional.map(value -> value.asInt(0)).orElse(0);
                if (quantity != 0) {
                    outputLines.add(new Line.LineBuilder()
                            .item(item)
                            .quantity(quantity)
                            .build());
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return outputLines;
    }
}
