package RefineryService.service;

import RefineryService.repository.RawResourcesRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RefineryCalculator {
    private final RawResourcesRepository rawResourcesRepository;

    @Builder
    private static class Line {
        String item;
        int quantity;
    }

    public List<String> calculator(String inputLines) {
        List<Line> lines = inputLinesParser(inputLines);
        List<String> outputList = new LinkedList<>();
        for (Line line : lines) {
            if (rawResourcesRepository.findByRawResourceName(line.item).isPresent()) {
                outputList.add(rawResourcesRepository.findByRawResourceName(line.item).get().getRawResourceName());
            }
        }
        return outputList;
    }

    private List<Line> inputLinesParser(String inputLines) {
        List<Line> outputLines = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(inputLines);
            for (JsonNode node : jsonNode) {
                Optional<JsonNode> itemOptional = Optional.ofNullable(node.get("item"));
                Optional<JsonNode> quantityOptional = Optional.ofNullable(node.get("quantity"));
                String item = itemOptional.isPresent() ? itemOptional.get().asText("item NOT found")
                        : "item NOT found";
                int quantity = quantityOptional.map(value -> value.asInt(0)).orElse(0);
                if (quantity != 0) {
                    outputLines.add(new Line.LineBuilder()
                            .item(item)
                            .quantity(quantity)
                            .build());
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return outputLines;
    }
}
