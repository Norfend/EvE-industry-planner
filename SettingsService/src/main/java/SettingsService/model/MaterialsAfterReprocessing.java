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
    public MaterialsAfterReprocessing() {
    }

    public MaterialsAfterReprocessing(Long materialsAfterReprocessingId,
                                      int materialsAfterReprocessingIconID,
                                      String materialsAfterReprocessingName) {
        MaterialsAfterReprocessingId = materialsAfterReprocessingId;
        MaterialsAfterReprocessingIconID = materialsAfterReprocessingIconID;
        MaterialsAfterReprocessingName = materialsAfterReprocessingName;
    }

    @Id
    @Column(name = "\"materials after reprocessing id\"")
    private Long MaterialsAfterReprocessingId;

    @Column(name = "\"materials after reprocessing icon id\"", nullable = false, unique = true)
    private int MaterialsAfterReprocessingIconID;

    @Column(name = "\"materials after reprocessing name\"", nullable = false, unique = true)
    private String MaterialsAfterReprocessingName;

    public Long getMaterialsAfterReprocessingId() {
        return MaterialsAfterReprocessingId;
    }

    public void setMaterialsAfterReprocessingId(Long materialsAfterReprocessingId) {
        MaterialsAfterReprocessingId = materialsAfterReprocessingId;
    }

    public int getMaterialsAfterReprocessingIconID() {
        return MaterialsAfterReprocessingIconID;
    }

    public void setMaterialsAfterReprocessingIconID(int materialsAfterReprocessingIconID) {
        MaterialsAfterReprocessingIconID = materialsAfterReprocessingIconID;
    }

    public String getMaterialsAfterReprocessingName() {
        return MaterialsAfterReprocessingName;
    }

    public void setMaterialsAfterReprocessingName(String materialsAfterReprocessingName) {
        MaterialsAfterReprocessingName = materialsAfterReprocessingName;
    }
}
