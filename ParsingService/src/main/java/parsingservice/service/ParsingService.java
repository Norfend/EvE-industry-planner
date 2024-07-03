package parsingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import parsingservice.parsingmodel.MarketGroup;
import parsingservice.parsingmodel.Type;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/*                else if (marketGroups.get(value.getMarketGroupID()) != null &&
                        marketGroups.get(value.getMarketGroupID()).getParentGroupID() == 2395) {
                    materials_after_reprocessing.put(key, value);
                }minerals = 1857
 *ice products = 1033
 *alloys & compounds = 1856
 *ice ores = 1855
 *raw moon materials = 501
 *parent group = 2395(moon)
 *parent group = 54(standard ores)*/

public class ParsingService {
    private final String filePath;
    private final Map<Integer, Type> materials_after_reprocessing;

    /**
     * @param filePath Path to the folder where the '*.yaml' files are located.
     */
    public ParsingService(String filePath) {
        this.filePath = filePath;
        this.materials_after_reprocessing = new HashMap<>();
        Map<Integer, Type> types = this.parseTypes();
        Map<Integer, MarketGroup> marketGroups = this.parseMarketGroups();
        types.forEach((key, value) -> {
            if (value.isPublished()) {
                if ((value.getMarketGroupID() == 1857 || value.getMarketGroupID() == 1033 || value.getMarketGroupID() == 501) &&
                        (key != 76374 && key != 48927)) {
                    materials_after_reprocessing.put(key, value);
                }
            }
        });
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

    public Map<Integer, Type> getMaterials_after_reprocessing() {
        return materials_after_reprocessing;
    }
}
