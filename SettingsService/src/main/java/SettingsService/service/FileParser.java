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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
                rawResources.add(new RawResources(portionSize, rawResourceName, iconID, Long.parseLong(entry.getKey())));
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
}
