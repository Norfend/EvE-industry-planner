package RefineryService.service;

/**
 * Temporary ENUM for characterYield and baseYield calculation*/
public enum Constants {
    REPROCESSING_SKILL(5),
    REPROCESSING_EFFICIENCY_SKILL(5),
    ORE_PROCESSING_SKILL(5),
    PROCESSING_IMPLANT(0.04);

    private final double value;

    Constants(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
