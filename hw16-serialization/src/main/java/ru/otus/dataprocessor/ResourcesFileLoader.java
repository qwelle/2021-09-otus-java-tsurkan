package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = System.getProperty("user.dir") + File.separator + "src\\test\\resources\\" + fileName;
    }

    @Override
    public List<Measurement> load() throws FileProcessException {
        //читает файл, парсит и возвращает результат
        File file = new File(fileName);
        List<Measurement> list = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(file);
            root.elements().forEachRemaining((element) -> {
                var measurement = new Measurement(element.at("/name").asText(), element.at("/value").asDouble());
                list.add(measurement);
            });
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
        return list;
    }
}
