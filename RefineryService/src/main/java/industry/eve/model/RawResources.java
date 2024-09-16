package industry.eve.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"raw resources\"",
        uniqueConstraints = {
        @UniqueConstraint(name = "\"unique raw resource name\"", columnNames = {"\"raw resource name\""})})
public class RawResources {
    @Id
    @Column(name = "\"raw resource id\"")
    private Long RawResourceId;

    @Column(name = "\"raw resource icon id\"", nullable = false)
    private int RawResourceIconID;

    @Column(name = "\"raw resource name\"", nullable = false, unique = true)
    private String RawResourceName;

    @Column(name = "\"raw resource portion size\"", nullable = false)
    private int rawResourcePortionSize;

    @Column(name = "\"raw resource refinery skill\"", nullable = false)
    private String rawResourceRefinerySkill;
}
