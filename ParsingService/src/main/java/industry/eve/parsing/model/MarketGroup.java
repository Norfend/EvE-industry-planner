package industry.eve.parsing.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketGroup {
    private int parentGroupID;

    @Override
    public String toString() {
        return parentGroupID + "";
    }
}
