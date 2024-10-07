package industry.eve.parsing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    private int materialTypeID;
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Material material = (Material) o;
        if (materialTypeID != material.materialTypeID) return false;
        return quantity == material.quantity;
    }

    @Override
    public int hashCode() {
        int result = materialTypeID;
        result = 31 * result + quantity;
        return result;
    }
}
