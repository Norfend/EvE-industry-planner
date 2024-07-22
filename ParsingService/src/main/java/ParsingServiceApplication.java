import service.ParsingService;

public class ParsingServiceApplication {
    public static void main(String[] args) {
        //CHANGE THAT!!!!
        ParsingService service = new ParsingService(System.getenv("file_path"));
        //System.out.println();
    }
}
