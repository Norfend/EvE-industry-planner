package RefineryService.service;

import RefineryService.model.CharacterInfo;
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
     * @param character A CharacterInfo used to get information about the character's skills
     * @return A list of strings representing the output materials and their corresponding quantities.
     *         If the input raw resource does not match any blueprint, it is skipped in the calculation.
     */
    public List<String> calculator(String inputLines, CharacterInfo character) {
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
                    String rawResourceRefinerySkill = resource.get().getRawResourceRefinerySkill();
                    double inputMaterialValue = blueprint.getQuantity() * numberOfPortions *
                                                getReprocessingRatio(rawResourceRefinerySkill, character);
                    if (materials.putIfAbsent(materialName, (int) inputMaterialValue) != null) {
                        materials.put(materialName, (int) inputMaterialValue + materials.get(materialName));
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

    private double getReprocessingRatio(String inputString, CharacterInfo character) {
        double baseYield = 0.5;
        int oreProcessingSkill;
        switch (inputString) {
            case "Abyssal Ore" -> oreProcessingSkill = character.abyssalOreProcessingSkill();
            case "Coherent Ore" -> oreProcessingSkill = character.coherentOreProcessingSkill();
            case "Common Moon Ore" -> oreProcessingSkill = character.commonMoonOreProcessingSkill();
            case "Complex Ore" -> oreProcessingSkill = character.complexOreProcessingSkill();
            case "Exceptional Moon Ore" -> oreProcessingSkill = character.exceptionalMoonOreProcessingSkill();
            case "Ice Ore" -> oreProcessingSkill = character.iceOreProcessingSkill();
            case "Mercoxit" -> oreProcessingSkill = character.mercoxitOreProcessingSkill();
            case "Rare Moon Ore" -> oreProcessingSkill = character.rareMoonOreProcessingSkill();
            case "Simple Ore" -> oreProcessingSkill = character.simpleOreProcessingSkill();
            case "Ubiquitous Moon Ore" -> oreProcessingSkill = character.ubiquitousMoonOreProcessingSkill();
            case "Uncommon Moon Ore" -> oreProcessingSkill = character.uncommonMoonOreProcessingSkill();
            case "Variegated Moon Ore" -> oreProcessingSkill = character.variegatedOreProcessingSkill();
            default -> oreProcessingSkill = 0;
        }
        double characterYield = (1 + character.reprocessingSkill() * 0.03) *
                                (1 + character.reprocessingEfficiencySkill() * 0.02) *
                                (1 + oreProcessingSkill * 0.02) *
                                (1 + character.reprocessingImplant());
        return baseYield * characterYield;
    }
}
