package parsingmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReprocessingBlueprint {
    private List<Material> materials = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReprocessingBlueprint that = (ReprocessingBlueprint) o;
        return materials.equals(that.materials);
    }

    @Override
    public int hashCode() {
        return materials.hashCode();
    }
}
