import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parsingmodel.Material;
import parsingmodel.Name;
import parsingmodel.ReprocessingBlueprint;
import parsingmodel.Type;
import service.ParsingService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ParsingServiceTest {
    @Test
    @DisplayName("Checking parsing of files from the developer")
    void parsingTesting() {
        String filePath = "src/test/resources";
        ParsingService parsingService = new ParsingService(filePath);
        Type materialAfterReprocessing = new Type(22, 1857, new Name("Tritanium"), 1, true, 0.01);
        Type rawResource = new Type(230, 516, new Name("Plagioclase"), 100, true, 0.35);
        Material materialOne = new Material(34, 175);
        Material materialTwo = new Material(36, 70);
        ArrayList<Material> materials = new ArrayList<>();
        materials.add(materialOne);
        materials.add(materialTwo);
        ReprocessingBlueprint reprocessingBlueprint = new ReprocessingBlueprint(materials);
        assertEquals(parsingService.getRawResources().get(18), rawResource);
        assertEquals(parsingService.getMaterialsAfterReprocessing().get(34), materialAfterReprocessing);
        assertEquals(parsingService.getReprocessingBlueprints().get(18), reprocessingBlueprint);
    }
}