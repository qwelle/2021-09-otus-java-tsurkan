package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        File outputFile = new File(fileName);
        try {
            mapper.writeValue(outputFile, data);
        } catch (Exception e) {
            throw new FileProcessException(e.getMessage());
        }

    }
}
