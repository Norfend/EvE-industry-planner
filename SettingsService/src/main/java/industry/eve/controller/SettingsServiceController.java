package industry.eve.controller;

import industry.eve.model.MaterialsAfterReprocessing;
import industry.eve.model.RawResources;
import industry.eve.model.ReprocessingBlueprint;
import industry.eve.service.FileParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "api/settings")
public class SettingsServiceController {
    private final FileParserService fileParserService;

    @Autowired
    public SettingsServiceController(FileParserService fileParserService) {
        this.fileParserService = fileParserService;
    }

    @PostMapping(path = "/raw-resources", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<RawResources>> setRawResources(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok().body(fileParserService.parseRawResourcesFile(file));
    }

    @PostMapping(path = "/materials-after-reprocessing", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<MaterialsAfterReprocessing>> setMaterialsAfterReprocessing
            (@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok().body(fileParserService.parseMaterialsAfterReprocessingFile(file));
    }

    @PostMapping(path = "/reprocessing-blueprint", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<ReprocessingBlueprint>> setReprocessingBlueprint
            (@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok().body(fileParserService.parseReprocessingBlueprintFile(file));
    }
}
