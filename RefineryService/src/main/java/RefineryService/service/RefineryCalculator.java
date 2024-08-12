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

    /**
     * Calculates the output materials after processing the input raw resources based on reprocessing blueprints.
     *
     * @param inputLines A string containing the input lines to be parsed and processed.
     * @return A list of strings representing the output materials and their corresponding quantities.
     *         If the input raw resource doesn't match any blueprint, it is skipped in the calculation.
     */
    public List<String> calculator(String inputLines) {
        List<Line> lines = inputLinesParser(inputLines);
        List<String> outputList = new LinkedList<>();
        Map<String, Integer> materials = new HashMap<>();
        for (Line line : lines) {
            Optional<RawResources> resource = rawResourcesRepository.findByRawResourceName(line.item);
            if (resource.isPresent() && resource.get().getRawResourcePortionSize() <= line.quantity) {
                int numberOfPortions = line.quantity / resource.get().getRawResourcePortionSize();
                List<ReprocessingBlueprint> reprocessingList = reprocessingBlueprintRepository
                                            .findByRawResourceId(resource.get())
                                            .orElseThrow();
                for (ReprocessingBlueprint blueprint: reprocessingList) {
                    String materialName = blueprint.getMaterialAfterReprocessingId().getMaterialsAfterReprocessingName();
                    double inputMaterialValue = materials.get(materialName) + blueprint.getQuantity() *
                                                numberOfPortions *
                                                getReprocessingRatio(materialName);
                    if (materials.putIfAbsent(materialName, (int) inputMaterialValue) != null) {
                        materials.put(materialName, (int) inputMaterialValue);
                    }
                }
            }
        }
        for (String material : materials.keySet()) {
            outputList.add(material + " " + materials.get(material));
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

    private double getReprocessingRatio(String inputString) {
        return 1.0;
    }
}
