package parsingmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketGroup {
    private int parentGroupID;

    public int getParentGroupID() {
        return parentGroupID;
    }

    public void setParentGroupID(int parentGroupID) {
        this.parentGroupID = parentGroupID;
    }

    @Override
    public String toString() {
        return parentGroupID + "";
    }
}
