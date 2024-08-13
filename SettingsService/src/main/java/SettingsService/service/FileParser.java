package SettingsService.service;

import SettingsService.model.MaterialsAfterReprocessing;
import SettingsService.model.RawResources;
import SettingsService.model.ReprocessingBlueprint;
import SettingsService.repository.MaterialsAfterReprocessingRepository;
import SettingsService.repository.RawResourcesRepository;
import SettingsService.repository.ReprocessingBlueprintRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class FileParser {
    private final RawResourcesRepository rawResourcesRepository;
    private final MaterialsAfterReprocessingRepository materialsAfterReprocessingRepository;
    private final ReprocessingBlueprintRepository reprocessingBlueprintRepository;

    @Autowired
    public FileParser(RawResourcesRepository rawResourcesRepository,
                      MaterialsAfterReprocessingRepository materialsAfterReprocessingRepository,
                      ReprocessingBlueprintRepository reprocessingBlueprintRepository) {
        this.rawResourcesRepository = rawResourcesRepository;
        this.materialsAfterReprocessingRepository = materialsAfterReprocessingRepository;
        this.reprocessingBlueprintRepository = reprocessingBlueprintRepository;
    }

    /**
     * Parses the raw resources from a JSON file.
     *
     * @param file The file to be parsed.
     * @return A list of RawResources objects. If an error occurs during parsing, an empty list is returned.
     */
    public List<RawResources> parseRawResourcesFile(MultipartFile file) {
        List<RawResources> rawResources = new LinkedList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(file.getInputStream());
            jsonNode.fields().forEachRemaining(entry -> {
                int iconID = entry.getValue().get("iconID").asInt();
                JsonNode name = entry.getValue().get("name");
                String rawResourceName = name.get("en").asText();
                int portionSize = entry.getValue().get("portionSize").asInt();
                String rawResourceRefinerySkill = rawResourceRefinerySkill(rawResourceName);
                rawResources.add(new RawResources(Long.parseLong(entry.getKey()),
                                                                 portionSize,
                                                                 rawResourceName,
                                                                 iconID,
                                                                 rawResourceRefinerySkill));
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return rawResourcesRepository.saveAll(rawResources);
    }

    /**
     * Parses the materials after reprocessing from a JSON file.
     *
     * @param file The multipart file to be parsed.
     * @return A list of MaterialsAfterReprocessing objects. If an error occurs during parsing, an empty list is returned.
     */
    public List<MaterialsAfterReprocessing> parseMaterialsAfterReprocessingFile(MultipartFile file) {
        List<MaterialsAfterReprocessing> materialsAfterReprocessing = new LinkedList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(file.getInputStream());
            jsonNode.fields().forEachRemaining(entry -> {
                int iconID = entry.getValue().get("iconID").asInt();
                JsonNode name = entry.getValue().get("name");
                String materialsAfterReprocessingName = name.get("en").asText();
                materialsAfterReprocessing.add(new MaterialsAfterReprocessing(
                        Long.parseLong(entry.getKey()),
                        iconID,
                        materialsAfterReprocessingName));
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return materialsAfterReprocessingRepository.saveAll(materialsAfterReprocessing);
    }

    /**
     * Parses the reprocessing blueprints from a JSON file.
     *
     * @param file The multipart file to be parsed.
     * @return A list of ReprocessingBlueprint objects. If an error occurs during parsing, an empty list is returned.
     */
    public List<ReprocessingBlueprint> parseReprocessingBlueprintFile(MultipartFile file) {
        List<ReprocessingBlueprint> reprocessingBlueprints = new LinkedList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(file.getInputStream());
            jsonNode.fields().forEachRemaining(entry -> {
                JsonNode firstWrap = entry.getValue().get("materials");
                Optional<RawResources> resourcesId = rawResourcesRepository.findById(Long.parseLong(entry.getKey()));
                for (var node : firstWrap) {
                    Optional<MaterialsAfterReprocessing> materialsId = materialsAfterReprocessingRepository.findById(node.get("materialTypeID").asLong());
                    int quantity = node.get("quantity").asInt();
                    ReprocessingBlueprint reprocessingBlueprint = new ReprocessingBlueprint(
                            resourcesId.orElseThrow(() -> new RuntimeException(entry.getKey() + "resourcesId")),
                            materialsId.orElseThrow(() -> new RuntimeException(entry.getKey() + "materialsId")),
                            quantity);
                    reprocessingBlueprints.add(reprocessingBlueprint);
                }
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return reprocessingBlueprintRepository.saveAll(reprocessingBlueprints);
    }

    /**
     * Determines the refinery skill category for a given raw resource name based on predefined ore groups.
     *
     * @param rawResourceName The name of the raw resource to classify.
     * @return A string representing the refinery skill category for the raw resource.
     *         If the resource does not match any known category, "Unknown" is returned.
     */
    @SuppressWarnings("SpellCheckingInspection")
    private String rawResourceRefinerySkill(String rawResourceName) {
        ArrayList<String> abyssalOre = new ArrayList<>(Arrays.asList("Bezdnacine", "Rakovene", "Talassonite"));
        ArrayList<String> coherentOre = new ArrayList<>(Arrays.asList("Hedbergite", "Hemorphite", "Jaspet", "Kernite",
                                                                      "Omber", "Ytirium", "Griemeer", "Nocxite"));
        ArrayList<String> commonMoonOre = new ArrayList<>(Arrays.asList("Cobaltite", "Euxenite", "Titanite", "Scheelite"));
        ArrayList<String> complexOre = new ArrayList<>(Arrays.asList("Arkonor", "Bistot", "Spodumain", "Eifyrium",
                                                                     "Ducinium", "Hezorime", "Ueganite"));
        ArrayList<String> exceptionalMoonOre = new ArrayList<>(Arrays.asList("Xenotime", "Monazite", "Loparite",
                                                                             "Ytterbite"));
        ArrayList<String> iceOre = new ArrayList<>(Arrays.asList("Blue Ice", "Clear Icicle", "Dark Glitter",
                                                                 "Enriched Clear Icicle", "Gelidus", "Glacial Mass",
                                                                 "Glare Crust", "Krystallos", "Pristine White Glaze",
                                                                 "Smooth Glacial Mass", "Thick Blue Ice", "White Glaze"));
        ArrayList<String> mercoxitOre = new ArrayList<>(List.of("Mercoxit"));
        ArrayList<String> rareMoonOre = new ArrayList<>(Arrays.asList("Carnotite", "Zircon", "Pollucite", "Cinnabar"));
        ArrayList<String> simpleOre = new ArrayList<>(Arrays.asList("Plagioclase", "Pyroxeres", "Scordite", "Veldspar",
                                                                    "Mordunium"));
        ArrayList<String> ubiquitousMoonOre = new ArrayList<>(Arrays.asList("Zeolites", "Sylvite", "Bitumens", "Coesite"));
        ArrayList<String> uncommonMoonOre = new ArrayList<>(Arrays.asList("Otavite", "Sperrylite", "Vanadinite", "Chromite"));
        ArrayList<String> variegatedOre = new ArrayList<>(Arrays.asList("Crokite", "Ochre", "Gneiss", "Kylixium"));
        Map<ArrayList<String>, String> ores = new HashMap<>();
        ores.put(abyssalOre, "Abyssal Ore"); ores.put(coherentOre, "Coherent Ore");
        ores.put(commonMoonOre, "Common Moon Ore"); ores.put(complexOre, "Complex Ore");
        ores.put(exceptionalMoonOre, "Exceptional Moon Ore"); ores.put(iceOre, "Ice Ore");
        ores.put(mercoxitOre, "Mercoxit"); ores.put(rareMoonOre, "Rare Moon Ore"); ores.put(simpleOre, "Simple Ore");
        ores.put(ubiquitousMoonOre, "Ubiquitous Moon Ore"); ores.put(uncommonMoonOre, "Uncommon Moon Ore");
        ores.put(variegatedOre, "Variegated Moon Ore");
        return ores.entrySet().stream()
                .filter(entry -> entry.getKey().stream().anyMatch(rawResourceName::contains))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("Unknown");
    }
}
