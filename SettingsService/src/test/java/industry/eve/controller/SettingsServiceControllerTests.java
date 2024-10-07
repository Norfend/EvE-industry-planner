package industry.eve.controller;

import industry.eve.model.RawResources;
import industry.eve.service.FileParserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(SettingsServiceController.class)
public class SettingsServiceControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileParserService fileParserService;

    @Test
    @DisplayName("Test of raw resources loading")
    public void setRawResources_RawResourcesLoading_RawResourcesLoadedCorrectly() throws Exception {
        //Arrange
        byte[] inputFile;
        try {
            inputFile = Files.readAllBytes(Path.of("src/test/resources/RawResources.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",inputFile);
        RawResources rawResource = new RawResources(62493L, 25177,
                "Compressed Replete Carnotite", 100, "Rare Moon Ore");
        given(fileParserService.parseRawResourcesFile(ArgumentMatchers.any())).willAnswer(invocation -> List.of(rawResource));
        //Act
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/settings/raw-resources")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andReturn();
        //Assert
        assertEquals(200, response.getResponse().getStatus());
    }
}
