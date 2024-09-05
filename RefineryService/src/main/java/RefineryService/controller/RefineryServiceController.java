package RefineryService.controller;

import RefineryService.model.CharacterInfo;
import RefineryService.service.RefineryCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/refinery")
public class RefineryServiceController {
    private final RefineryCalculator refineryCalculator;

    @Autowired
    public RefineryServiceController(RefineryCalculator refineryCalculator) {
        this.refineryCalculator = refineryCalculator;
    }

    @GetMapping(path = "/calculator")
    public ResponseEntity<List<String>> rawResourcesConverter(@RequestBody String inputLines,
                                                              @RequestHeader(value="characterInfo") CharacterInfo character) {
        return ResponseEntity.ok().body(refineryCalculator.calculator(inputLines, character));
    }
}
