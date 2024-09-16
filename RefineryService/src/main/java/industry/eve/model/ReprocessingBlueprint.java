package industry.eve.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"reprocessing blueprint\"")
public class ReprocessingBlueprint {
    public ReprocessingBlueprint(RawResources rawResourceId,
                                 MaterialsAfterReprocessing materialAfterReprocessingId,
                                 int quantity) {
        this.rawResourceId = rawResourceId;
        MaterialAfterReprocessingId = materialAfterReprocessingId;
        this.quantity = quantity;
    }

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

    @ManyToOne
    @JoinColumn(
            name = "\"raw resource id\"",
            nullable = false,
            foreignKey = @ForeignKey(name = "\"raw resource FK\""))
    private RawResources rawResourceId;

    @ManyToOne
    @JoinColumn(
            name = "\"materials after reprocessing id\"",
            nullable = false,
            foreignKey = @ForeignKey(name = "\"materials after reprocessing FK\""))
    private MaterialsAfterReprocessing MaterialAfterReprocessingId;

    @Column(nullable = false)
    private int quantity;
}
