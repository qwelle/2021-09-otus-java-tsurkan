package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        var processedData = new TreeMap<String, Double>();
        data.stream().forEach((measurement) -> {
            var k = measurement.getName();
            var v = measurement.getValue();
            processedData.merge(k, v, (oldV, val) -> v + oldV);
        });
        return processedData;
    }
}
