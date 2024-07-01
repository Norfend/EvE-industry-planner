import model.Type;
import service.ParsingService;

import java.util.Map;


public class Main {
    public static void main(String[] args) {
        Map<Integer, Type> types = ParsingService.parseTypes("B:\\EvE-API\\sde\\fsd");
        types.forEach((key, value) -> {
            if (value.getPortionSize() == 100) {
                System.out.println("typeID = " + key + " " + value);
            }
        });
    }
}
