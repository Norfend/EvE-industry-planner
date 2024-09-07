package parsingmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Type {
    private int iconID;
    private int marketGroupID;
    private Name name;
    private int portionSize;
    private boolean published;
    private double volume;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Type type)) return false;

        return getIconID() == type.getIconID() && getMarketGroupID() == type.getMarketGroupID() && getPortionSize() == type.getPortionSize() && isPublished() == type.isPublished() && Double.compare(getVolume(), type.getVolume()) == 0 && getName().equals(type.getName());
    }

    @Override
    public int hashCode() {
        int result = getIconID();
        result = 31 * result + getMarketGroupID();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getPortionSize();
        result = 31 * result + Boolean.hashCode(isPublished());
        result = 31 * result + Double.hashCode(getVolume());
        return result;
    }

    @Override
    public String toString() {
        return  "iconID = " + iconID +
                ", marketGroupID = " + marketGroupID +
                ", name = " + name +
                ", portionSize = " + portionSize +
                ", published = " + published +
                ", volume = " + volume;
    }
}
