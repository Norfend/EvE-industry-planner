package SettingsService.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "\"raw resources\"",
        uniqueConstraints = {
        @UniqueConstraint(name = "\"unique raw resource name\"", columnNames = {"\"raw resource name\""})})
public class RawResources {
    public RawResources() {
    }

    public RawResources(int rawResourcePortionSize, String rawResourceName, int rawResourceIconID, Long rawResourceId) {
        RawResourcePortionSize = rawResourcePortionSize;
        RawResourceName = rawResourceName;
        RawResourceIconID = rawResourceIconID;
        RawResourceId = rawResourceId;
    }

    @Id
    @Column(name = "\"raw resource id\"")
    private Long RawResourceId;

    @Column(name = "\"raw resource icon id\"", nullable = false)
    private int RawResourceIconID;

    @Column(name = "\"raw resource name\"", nullable = false, unique = true)
    private String RawResourceName;

    @Column(name = "\"raw resource portion size\"", nullable = false)
    private int RawResourcePortionSize;

    @OneToMany(mappedBy = "rawResourceId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReprocessingBlueprint> reprocessingBlueprint = new HashSet<>();

    public int getRawResourcePortionSize() {
        return RawResourcePortionSize;
    }

    public void setRawResourcePortionSize(int rawResourcePortionSize) {
        RawResourcePortionSize = rawResourcePortionSize;
    }

    public String getRawResourceName() {
        return RawResourceName;
    }

    public void setRawResourceName(String rawResourceName) {
        RawResourceName = rawResourceName;
    }

    public int getRawResourceIconID() {
        return RawResourceIconID;
    }

    public void setRawResourceIconID(int rawResourceIconID) {
        RawResourceIconID = rawResourceIconID;
    }

    public Long getRawResourceId() {
        return RawResourceId;
    }

    public void setRawResourceId(Long rawResourceId) {
        RawResourceId = rawResourceId;
    }

    public Set<ReprocessingBlueprint> getReprocessingBlueprint() {
        return reprocessingBlueprint;
    }

    public void setReprocessingBlueprint(Set<ReprocessingBlueprint> reprocessingBlueprint) {
        this.reprocessingBlueprint = reprocessingBlueprint;
    }

    public void addResourceMaterial(ReprocessingBlueprint blueprint) {
        reprocessingBlueprint.add(blueprint);
        blueprint.setRawResourceId(this);
    }

    public void removeResourceMaterial(ReprocessingBlueprint blueprint) {
        reprocessingBlueprint.remove(blueprint);
        blueprint.setRawResourceId(null);
    }
}
