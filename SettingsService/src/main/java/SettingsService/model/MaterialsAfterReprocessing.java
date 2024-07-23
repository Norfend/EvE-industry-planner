package SettingsService.model;

import jakarta.persistence.*;

@Entity
@Table(name = "\"materials after reprocessing\"",
        uniqueConstraints = {
                @UniqueConstraint(name = "\"unique materials after reprocessing iconID\"",
                                  columnNames = {"\"materials after reprocessing icon id\""}),
                @UniqueConstraint(name = "\"unique materials after reprocessing name\"",
                                  columnNames = {"\"materials after reprocessing name\""})})
public class MaterialsAfterReprocessing {
    @Id
    @Column(name = "\"materials after reprocessing id\"")
    private Long MaterialsAfterReprocessingId;
    @Column(name = "\"materials after reprocessing icon id\"", nullable = false, unique = true)
    private int MaterialsAfterReprocessingIconID;
    @Column(name = "\"materials after reprocessing name\"", nullable = false, unique = true)
    private String MaterialsAfterReprocessingName;
}
