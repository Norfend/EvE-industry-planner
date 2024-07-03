package parsingservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import parsingservice.model.MaterialsAfterReprocessing;
import parsingservice.repository.MaterialsAfterReprocessingRepository;
import parsingservice.service.ParsingService;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartUpApplicationRunner implements ApplicationRunner {
    @Value("${file.path}")
    private String filePath;

    @Autowired
    private MaterialsAfterReprocessingRepository MARRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ParsingService parsingService = new ParsingService(filePath);
        List<MaterialsAfterReprocessing> MARs = new ArrayList<>();
        parsingService.getMaterials_after_reprocessing().forEach((key, value) -> {
            MARs.add(new MaterialsAfterReprocessing(key, value.getName().getEn(), value.getVolume(), value.getIconID()));
        });
        MARRepository.saveAll(MARs);
    }
}
