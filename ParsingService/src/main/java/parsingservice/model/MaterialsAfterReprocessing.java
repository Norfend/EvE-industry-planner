package parsingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "materials_after_reprocessing")
public class MaterialsAfterReprocessing {
    public MaterialsAfterReprocessing() {
    }

    public MaterialsAfterReprocessing(long materials_id, String materials_name, double materials_volume, int materials_iconID) {
        this.materials_id = materials_id;
        this.materials_name = materials_name;
        this.materials_volume = materials_volume;
        this.materials_iconID = materials_iconID;
    }

    @Id
    @Column(name = "Materials ID")
    private long materials_id;

    @Column(name = "Materials name", nullable = false, unique = true)
    private String materials_name;

    @Column(name = "Materials volume", nullable = false)
    private double materials_volume;

    @Column(name = "Materials iconID", nullable = false, unique = true)
    private int materials_iconID;

    public int getMaterials_iconID() {
        return materials_iconID;
    }

    public void setMaterials_iconID(int materials_iconID) {
        this.materials_iconID = materials_iconID;
    }

    public double getMaterials_volume() {
        return materials_volume;
    }

    public void setMaterials_volume(double materials_volume) {
        this.materials_volume = materials_volume;
    }

    public String getMaterials_name() {
        return materials_name;
    }

    public void setMaterials_name(String materials_name) {
        this.materials_name = materials_name;
    }

    public long getMaterials_id() {
        return materials_id;
    }

    public void setMaterials_id(long materials_id) {
        this.materials_id = materials_id;
    }
}
