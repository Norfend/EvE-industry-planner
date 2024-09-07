package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.yaml.snakeyaml.LoaderOptions;
import parsingmodel.MarketGroup;
import parsingmodel.ReprocessingBlueprint;
import parsingmodel.Type;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Here are the number of groups that I want to parse from files:
 * <ul>
 *   <li>Minerals: 1857</li>
 *   <li>Ice Products: 1033</li>
 *   <li>Alloys & Compounds: 1856</li>
 *   <li>Ice Ores: 1855</li>
 *   <li>Raw Moon Materials: 501</li>
 *   <li>Moon Ores (parent group): 2395</li>
 *   <li>Standard Ores (parent group) 54</li>
 * </ul>
 * Here are the number of groups that I don't want to parse from files:
 * <ul>
 *   <li>Nephrite: 60771</li>
 *   <li>Hiemal Tricarboxyl Vapor: 49787</li>
 *   <li>Nesosilicate Rakovene: 76373</li>
 *   <li>Neo-Jadarite: 76374</li>
 *   <li>Chromodynamic Tricarboxyls: 48927</li>
 *   <li>Hedbergite ( copy ): 82015</li>
 *   <li>Ores with "Batch" in the name</li>
 * </ul>*/
public class ParsingService {
    private final String filePath;
    private final Map<Integer, Type> materialsAfterReprocessing;
    private final Map<Integer, Type> rawResources;
    private final Map<Integer, ReprocessingBlueprint> reprocessingBlueprints;

    /**
     * @param filePath Path to the folder where the '*.yaml' files are located.
     */
    public ParsingService(String filePath) {
        this.filePath = filePath;
        this.materialsAfterReprocessing = new HashMap<>();
        this.rawResources = new HashMap<>();
        Map<Integer, Type> types = this.parseTypes();
        Map<Integer, MarketGroup> marketGroups = this.parseMarketGroups();
        types.forEach((key, value) -> {
            if (value.isPublished()) {
                if ((value.getMarketGroupID() == 1857
                        || value.getMarketGroupID() == 1033
                        || value.getMarketGroupID() == 501)
                        && (key != 76374 && key != 48927)) {
                    materialsAfterReprocessing.put(key, value);
                }
                else if (marketGroups.get(value.getMarketGroupID()) != null
                        && (marketGroups.get(value.getMarketGroupID()).getParentGroupID() == 2395
                        || marketGroups.get(value.getMarketGroupID()).getParentGroupID() == 54
                        || value.getMarketGroupID() == 1856
                        || value.getMarketGroupID() == 1855)
                        && (key != 60771 && key != 49787 && key != 76373 && key != 82015
                            && !value.getName().getEn().contains("Batch"))) {
                    rawResources.put(key, value);
                }
            }
        });
        this.reprocessingBlueprints = this.parseReprocessingBlueprints().entrySet()
                .stream()
                .filter(entry -> rawResources.containsKey(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Parses a YAML file containing types and returns a map of type IDs to Type objects.
     *
     * @return A map where the keys are integer IDs and the values are Type objects.
     *         If an error occurs during parsing, an empty map is returned.
     */
    private Map<Integer, Type> parseTypes() {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setCodePointLimit(150 * 1024 * 1024); //for parsing 150 Mb of data
        YAMLFactory yamlFactory = YAMLFactory.builder()
                .loaderOptions(loaderOptions)
                .build();
        ObjectMapper mapper = new ObjectMapper(yamlFactory);
        Map<Integer, Type> IDs = new HashMap<>();
        try {
            IDs = mapper.readValue(new File(this.filePath + "\\types.yaml"),
                    mapper.getTypeFactory().constructMapType(Map.class, Integer.class, Type.class));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return IDs;
    }

    /**
     * Parses a YAML file containing market groups and returns a map of market group IDs to MarketGroup objects.
     *
     * @return A map where the keys are integer IDs and the values are MarketGroup objects.
     *         If an error occurs during parsing, an empty map is returned.
     */
    private Map<Integer, MarketGroup> parseMarketGroups() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Map<Integer, MarketGroup> marketGroups = new HashMap<>();
        try {
            marketGroups = mapper.readValue(new File(this.filePath + "\\marketGroups.yaml"),
                    mapper.getTypeFactory().constructMapType(Map.class, Integer.class, MarketGroup.class));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return marketGroups;
    }

    /**
     * Parses the reprocessing blueprints from a YAML file.
     *
     * @return A map where the keys are integer IDs and the values are ReprocessingBlueprint objects.
     *         If an error occurs during parsing, an empty map is returned.
     */
    private Map<Integer, ReprocessingBlueprint> parseReprocessingBlueprints() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Map<Integer, ReprocessingBlueprint> reprocessingBlueprints = new HashMap<>();
        try {
            reprocessingBlueprints = mapper.readValue(new File(this.filePath + "\\typeMaterials.yaml"),
                    mapper.getTypeFactory().constructMapType(Map.class, Integer.class, ReprocessingBlueprint.class));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return reprocessingBlueprints;
    }

    public Map<Integer, Type> getMaterialsAfterReprocessing() {
        return materialsAfterReprocessing;
    }

    public Map<Integer, Type> getRawResources() {
        return rawResources;
    }

    public Map<Integer, ReprocessingBlueprint> getReprocessingBlueprints() {
        return reprocessingBlueprints;
    }
}
