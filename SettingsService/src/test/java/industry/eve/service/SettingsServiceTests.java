package industry.eve.service;

import industry.eve.model.MaterialsAfterReprocessing;
import industry.eve.model.RawResources;
import industry.eve.model.ReprocessingBlueprint;
import industry.eve.repository.MaterialsAfterReprocessingRepository;
import industry.eve.repository.RawResourcesRepository;
import industry.eve.repository.ReprocessingBlueprintRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link FileParserService} class, responsible for testing the parsing of various files
 * related to reprocessing and raw resources.
 */
@ExtendWith(MockitoExtension.class)
public class SettingsServiceTests {
    @Mock
    private MaterialsAfterReprocessingRepository materialsAfterReprocessingRepository;
    @Mock
    private RawResourcesRepository rawResourcesRepository;
    @Mock
    private ReprocessingBlueprintRepository reprocessingBlueprintRepository;

    @InjectMocks
    private FileParserService fileParserService;

    /**
     * Tests the parsing of the "materials after reprocessing" file.
     * This method verifies if the file is correctly parsed and the corresponding {@link MaterialsAfterReprocessing}
     * objects are saved and returned.
     */
    @Test
    @DisplayName("Testing of \"materials after reprocessing\" parser")
    public void parseMaterialsAfterReprocessingFileTest() {
        //Arrange
        byte[] inputFile;
        try {
            inputFile = Files.readAllBytes(Path.of("src/test/resources/MaterialsAfterReprocessing.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MockMultipartFile mockMultipartFile = new MockMultipartFile("tempFileName",inputFile);
        MaterialsAfterReprocessing materialAfterReprocessing = new MaterialsAfterReprocessing(16641L, 2569, "Chromium");
        //Act
        List<MaterialsAfterReprocessing> materialsAfterReprocessing = List.of(materialAfterReprocessing);
        when(materialsAfterReprocessingRepository.saveAll(Mockito.anyCollection()))
                                                 .thenReturn(materialsAfterReprocessing);
        List<MaterialsAfterReprocessing> materialsAfterReprocessingList =
                fileParserService.parseMaterialsAfterReprocessingFile(mockMultipartFile);
        //Assert
        assertEquals(1, materialsAfterReprocessingList.size());
        assertEquals(materialAfterReprocessing, materialsAfterReprocessingList.getFirst());
    }

    /**
     * Tests the parsing of the "raw resources" file.
     * This method verifies if the file is correctly parsed and the corresponding {@link RawResources}
     * objects are saved and returned.
     */
    @Test
    @DisplayName("Testing of \"raw resources\" parser")
    public void parseRawResourcesTest() {
        //Arrange
        byte[] inputFile;
        try {
            inputFile = Files.readAllBytes(Path.of("src/test/resources/RawResources.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MockMultipartFile mockMultipartFile = new MockMultipartFile("tempFileName",inputFile);
        RawResources rawResource = new RawResources(62493L, 25177,
                "Compressed Replete Carnotite", 100, "Rare Moon Ore");
        //Act
        List<RawResources> testingRawResourcesList = List.of(rawResource);
        when(rawResourcesRepository.saveAll(Mockito.anyCollection()))
                .thenReturn(testingRawResourcesList);
        List<RawResources> rawResources =
                fileParserService.parseRawResourcesFile(mockMultipartFile);
        //Assert
        assertEquals(1, rawResources.size());
        assertEquals(rawResource, rawResources.getFirst());
    }

    /**
     * Tests the parsing of the "reprocessing blueprint" file.
     * This method verifies if the file is correctly parsed and the corresponding {@link ReprocessingBlueprint}
     * objects are saved and returned.
     */
    @Test
    @DisplayName("Testing of \"reprocessing blueprint\" parser")
    public void parseReprocessingBlueprintTest() {
        //Arrange
        byte[] inputFile;
        try {
            inputFile = Files.readAllBytes(Path.of("src/test/resources/ReprocessingBlueprints.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MockMultipartFile mockMultipartFile = new MockMultipartFile("tempFileName",inputFile);

        RawResources testingRawResources = new RawResources();
        testingRawResources.setRawResourceId(62464L);

        MaterialsAfterReprocessing testingMaterialAfterReprocessing = new MaterialsAfterReprocessing();
        testingMaterialAfterReprocessing.setMaterialsAfterReprocessingId(35L);

        ReprocessingBlueprint reprocessingBlueprint = new ReprocessingBlueprint();
        reprocessingBlueprint.setMaterialAfterReprocessingId(reprocessingBlueprint.getMaterialAfterReprocessingId());
        reprocessingBlueprint.setQuantity(9200);
        reprocessingBlueprint.setRawResourceId(testingRawResources);
        reprocessingBlueprint.setMaterialAfterReprocessingId(testingMaterialAfterReprocessing);
        //Act
        when(rawResourcesRepository.findById(Mockito.any())).thenReturn(Optional.of(testingRawResources));
        List<ReprocessingBlueprint> testingReprocessingBlueprintList = List.of(reprocessingBlueprint);
        when(reprocessingBlueprintRepository.saveAll(Mockito.anyCollection()))
                .thenReturn(testingReprocessingBlueprintList);
        when(materialsAfterReprocessingRepository.findById(Mockito.any())).thenReturn(Optional.of(testingMaterialAfterReprocessing));
        List<ReprocessingBlueprint> reprocessingBlueprintList =
                fileParserService.parseReprocessingBlueprintFile(mockMultipartFile);
        //Assert
        assertEquals(1, reprocessingBlueprintList.size());
        assertEquals(reprocessingBlueprint, reprocessingBlueprintList.getFirst());
    }
}
