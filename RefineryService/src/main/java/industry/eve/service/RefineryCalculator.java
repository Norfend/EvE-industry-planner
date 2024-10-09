package industry.eve.service;

import industry.eve.model.MaterialsAfterReprocessing;
import industry.eve.model.RawResources;
import industry.eve.model.ReprocessingBlueprint;
import industry.eve.repository.MaterialsAfterReprocessingRepository;
import industry.eve.repository.RawResourcesRepository;
import industry.eve.repository.ReprocessingBlueprintRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class RefineryCalculator {
    private final MaterialsAfterReprocessingRepository mARRepository;
    private final RawResourcesRepository rawResourcesRepository;
    private final ReprocessingBlueprintRepository reprocessingBlueprintRepository;

    /**
     * Calculates the output materials after processing the input raw resources based on reprocessing blueprints.
     *
     * @param inputLines A List<ResourcesLine> containing the input lines to be processed.
     * @param character A CharacterInfo used to get information about the character's skills
     * @param baseYieldInfo A BaseYieldInfo used to get information about the structure, where a process will perform
     * @return A list of strings representing the output materials and their corresponding quantities.
     *         If the input raw resource does not match any blueprint, it is skipped in the calculation.
     */
    public List<String> calculator(List<ResourcesLine> inputLines, CharacterInfo character, BaseYieldInfo baseYieldInfo) {
        List<String> outputList = new LinkedList<>();
        Map<String, Integer> materials = new HashMap<>();
        for (ResourcesLine line : inputLines) {
            Optional<RawResources> resource = rawResourcesRepository.findByRawResourceName(line.item());
            if (resource.isPresent() && resource.get().getRawResourcePortionSize() <= line.quantity()) {
                int numberOfPortions = line.quantity() / resource.get().getRawResourcePortionSize();
                List<ReprocessingBlueprint> reprocessingList = reprocessingBlueprintRepository
                                            .findByRawResourceId(resource.get())
                                            .orElseThrow();
                for (ReprocessingBlueprint blueprint: reprocessingList) {
                    String materialName = blueprint.getMaterialAfterReprocessingId().getMaterialsAfterReprocessingName();
                    String rawResourceRefinerySkill = resource.get().getRawResourceRefinerySkill();
                    double inputMaterialValue = blueprint.getQuantity() * numberOfPortions *
                                                getReprocessingRatio(rawResourceRefinerySkill, character, baseYieldInfo);
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

    public List<String> reverseCalculator(List<ResourcesLine> inputLines, CharacterInfo character,
                                          BaseYieldInfo baseYieldInfo) {
        List<String> outputList = new LinkedList<>();
        for (ResourcesLine line : inputLines) {
            Optional<MaterialsAfterReprocessing> material = mARRepository.findByMaterialsAfterReprocessingName(line.item());
            if (material.isPresent()) {
                System.out.println();
            }
        }
        return outputList;
    }

    private double getReprocessingRatio(String inputString, CharacterInfo character, BaseYieldInfo baseYieldInfo) {
        double baseYield = (50 + baseYieldInfo.rigModifier())*(1 + baseYieldInfo.securityModifier())*
                           (1 + baseYieldInfo.structureModifier());
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
