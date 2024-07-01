package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import model.Type;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParsingService {
    /**
     * Parses a YAML file containing types and returns a map of type IDs to Type objects.
     *
     * @param filePath The file path where the 'types.yaml' file is located.
     * @return A map where the keys are integer IDs and the values are Type objects.
     *         If an error occurs during parsing, an empty map is returned.
     */
    public static Map<Integer, Type> parseTypes(String filePath) {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setCodePointLimit(150 * 1024 * 1024); //for parsing 150 Mb of data
        YAMLFactory yamlFactory = YAMLFactory.builder()
                .loaderOptions(loaderOptions)
                .build();
        ObjectMapper mapper = new ObjectMapper(yamlFactory);
        Map<Integer, Type> IDs = new HashMap<>();
        try {
            IDs = mapper.readValue(new File(filePath + "\\types.yaml"),
                    mapper.getTypeFactory().constructMapType(Map.class, Integer.class, Type.class));
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return IDs;
    }
}
