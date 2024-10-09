package industry.eve.service;

import java.util.List;

public record RefineryCalculatorInputWrapper(List<ResourcesLine> inputResources, CharacterInfo characterInfo,
                                             BaseYieldInfo baseYieldInfo) {
}
