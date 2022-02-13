package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final ObjectMapper mapper = new ObjectMapper();
    private final File file;

    public ResourcesFileLoader(String fileName) throws URISyntaxException {
        this.file = new File(getClass().getClassLoader().getResource(fileName).toURI());
    }

    @Override
    public List<Measurement> load() throws FileProcessException {
        //читает файл, парсит и возвращает результат
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
