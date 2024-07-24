package SettingsService.service;

import SettingsService.model.MaterialsAfterReprocessing;
import SettingsService.model.RawResources;
import SettingsService.repository.MaterialsAfterReprocessingRepository;
import SettingsService.repository.RawResourcesRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

@Service
public class FileParser {
    private final RawResourcesRepository rawResourcesRepository;
    private final MaterialsAfterReprocessingRepository materialsAfterReprocessingRepository;

    @Autowired
    public FileParser(RawResourcesRepository rawResourcesRepository,
                      MaterialsAfterReprocessingRepository materialsAfterReprocessingRepository) {
        this.rawResourcesRepository = rawResourcesRepository;
        this.materialsAfterReprocessingRepository = materialsAfterReprocessingRepository;
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
}
