package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Type {
    private int iconID;
    private Name name;
    private int portionSize;
    private double volume;

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public int getPortionSize() {
        return portionSize;
    }

    public void setPortionSize(int portionSize) {
        this.portionSize = portionSize;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Type type)) return false;

        return iconID == type.iconID && portionSize == type.portionSize && Double.compare(volume, type.volume) == 0 && name.equals(type.name);
    }

    @Override
    public int hashCode() {
        int result = iconID;
        result = 31 * result + name.hashCode();
        result = 31 * result + portionSize;
        result = 31 * result + Double.hashCode(volume);
        return result;
    }

    @Override
    public String toString() {
        return "iconID = " + iconID +
                " name = " + name +
                " portionSize = " + portionSize +
                " volume = " + volume;
    }
}
