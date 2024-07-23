package SettingsService.model;

import jakarta.persistence.*;

@Entity
@Table(name = "\"reprocessing blueprint\"")
public class ReprocessingBlueprint {
    @Id
    @SequenceGenerator(
            name = "\"reprocessing blueprint id sequence\"",
            sequenceName = "\"reprocessing blueprint id sequence\"",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "\"reprocessing blueprint id sequence\""
    )
    private Long ReprocessingBlueprintId;
    @Column(name = "\"raw material id\"", nullable = false)
    private Long RawMaterialId;
    @Column(name = "\"materials after reprocessing id\"", nullable = false)
    private Long MaterialAfterReprocessingId;
    @Column(nullable = false)
    private int quantity;
}
