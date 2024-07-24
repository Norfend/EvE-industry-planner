package SettingsService.controller;

import SettingsService.model.MaterialsAfterReprocessing;
import SettingsService.model.RawResources;
import SettingsService.service.FileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "api/settings")
public class SettingsServiceController {
    private final FileParser fileParser;

    @Autowired
    public SettingsServiceController(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @PostMapping(path = "/raw-resources", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<RawResources>> setRawResources(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok().body(fileParser.parseRawResourcesFile(file));
    }

    @PostMapping(path = "/materials-after-reprocessing", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<MaterialsAfterReprocessing>> setMaterialsAfterReprocessing
            (@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok().body(fileParser.parseMaterialsAfterReprocessingFile(file));
    }
}
